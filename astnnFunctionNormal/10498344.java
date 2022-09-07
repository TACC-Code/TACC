class BackupThread extends Thread {
    public static final URL Call(String method, URL target, HttpMessage headers, File source, OutputStream dst) throws java.io.IOException {
        if (null != source) {
            String path = target.getPath();
            if (UrlValidMultisource(path)) {
                String querystring = target.getQuery();
                if (null != querystring) {
                    path = Chbuf.cat(source.getPath(), "?", querystring);
                    target = new URL(Chbuf.fcat(target.toExternalForm(), path));
                } else target = new URL(Chbuf.fcat(target.toExternalForm(), source.getPath()));
            }
        }
        HttpURLConnection http = (HttpURLConnection) target.openConnection();
        try {
            http.setRequestMethod(method);
            if (null != headers) {
                for (int cc = 0, count = headers.countHeaders(); cc < count; cc++) {
                    Header header = headers.getHeader(cc);
                    if (Include(header)) http.setRequestProperty(header.getName(), header.getValue());
                }
            }
            if (null != source && MethodValidSource(method)) {
                http.setDoInput(true);
                Bbuf content = new Bbuf(source);
                int contentLength = content.length();
                if (0 < contentLength) {
                    if (headers.hasNotContentType()) {
                        alto.lang.Type contentType = Type.Tools.Of(source.getName());
                        if (null != contentType) http.setRequestProperty("Content-Type", contentType.toString());
                    }
                    boolean overwrite = OptionOverwrite(headers);
                    long last = source.lastModified();
                    if (0L < last) {
                        String mod = Date.ToString(last);
                        http.setRequestProperty("Last-Modified", mod);
                        String etag = alto.sys.File.ETag(source.length(), last);
                        http.setRequestProperty("ETag", etag);
                        if (!overwrite) {
                            http.setRequestProperty("If-Unmodified-Since", mod);
                            http.setRequestProperty("If-None-Match", etag);
                        }
                    }
                    http.setRequestProperty("Content-Length", String.valueOf(contentLength));
                    OutputStream out = http.getOutputStream();
                    try {
                        content.writeTo(out);
                    } finally {
                        out.close();
                    }
                }
            }
            Authenticate(headers, http);
            http.connect();
            int status = http.getResponseCode();
            int contentLength = http.getContentLength();
            if (0 < contentLength && (!"HEAD".equalsIgnoreCase(method))) {
                InputStream in = (InputStream) http.getInputStream();
                if (null == dst) dst = System.out;
                byte[] iob = new byte[0x100];
                int read, remainder = contentLength, bufl = 0x100;
                while (0 < (read = in.read(iob, 0, bufl))) {
                    dst.write(iob, 0, read);
                    remainder -= read;
                    if (remainder < bufl) {
                        if (1 > remainder) break; else bufl = remainder;
                    }
                }
                if (0 < remainder) throw new java.io.IOException("Read '" + remainder + "' bytes short of '" + contentLength + "' from '" + status + " " + http.getResponseMessage() + "' @ " + target);
            }
            switch(status) {
                case 200:
                case 201:
                case 204:
                case 304:
                    return target;
                default:
                    throw new java.io.IOException(status + " " + http.getResponseMessage() + " @ " + target);
            }
        } finally {
            http.disconnect();
        }
    }
}
