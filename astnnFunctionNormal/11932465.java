class BackupThread extends Thread {
    public XmlObject parse(URL url, SchemaType type, XmlOptions options) throws XmlException, IOException {
        if (options == null) {
            options = new XmlOptions();
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, url.toString());
        } else if (!options.hasOption(XmlOptions.DOCUMENT_SOURCE_NAME)) {
            options = new XmlOptions(options);
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, url.toString());
        }
        URLConnection conn = null;
        InputStream stream = null;
        download: try {
            boolean redirected = false;
            int count = 0;
            do {
                conn = url.openConnection();
                conn.addRequestProperty("User-Agent", USER_AGENT);
                conn.addRequestProperty("Accept", "application/xml, text/xml, */*");
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpcon = (HttpURLConnection) conn;
                    int code = httpcon.getResponseCode();
                    redirected = (code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP);
                    if (redirected && count > 5) redirected = false;
                    if (redirected) {
                        String newLocation = httpcon.getHeaderField("Location");
                        if (newLocation == null) redirected = false; else {
                            url = new URL(newLocation);
                            count++;
                        }
                    }
                }
            } while (redirected);
            stream = conn.getInputStream();
            return parse(stream, type, options);
        } finally {
            if (stream != null) stream.close();
        }
    }
}
