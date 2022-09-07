class BackupThread extends Thread {
    public static URL upload(InputStream src, String id, String filename, String contentype, URL url, int timeout, String user, String password) {
        Calendar c = new GregorianCalendar();
        String boundary = String.format("---------------------------%x", c.getTimeInMillis() / 1000);
        HttpURLConnection hc;
        try {
            hc = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            return null;
        }
        try {
            hc.setRequestMethod("POST");
        } catch (ProtocolException e) {
            return null;
        }
        hc.setInstanceFollowRedirects(false);
        hc.setDoOutput(true);
        hc.setConnectTimeout(timeout);
        hc.setReadTimeout(timeout);
        hc.setRequestProperty("Content-Type", String.format("multipart/form-data; boundary=%s", boundary));
        boundary = "--" + boundary;
        StringBuilder st = new StringBuilder();
        String req = String.format("<SOAP-ENV:Envelope" + " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" + " xmlns:m=\"urn:Meetup/Lite\">" + "<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" + "<m:trace><user>%s</user><password>%s</password></m:trace></SOAP-ENV:Body></SOAP-ENV:Envelope>", user, password);
        st.append(String.format("%s\r\nContent-Disposition: form-data; name=\"req\"\r\n\r\n%s\r\n" + "%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", boundary, req, boundary, id, filename, contentype));
        StringBuilder en = new StringBuilder();
        en.append(String.format("\r\n%s--\r\n", boundary));
        byte[] stb = st.toString().getBytes();
        byte[] enb = en.toString().getBytes();
        int size = stb.length + enb.length;
        int b;
        Vector<Byte> vb = new Vector<Byte>();
        try {
            while (true) {
                b = src.read();
                if (b == -1) break;
                vb.add((byte) b);
            }
        } catch (IOException e) {
            return null;
        }
        size += vb.size();
        hc.setRequestProperty("Content-Length", String.format("%d", size));
        try {
            hc.connect();
        } catch (IOException e) {
            return null;
        }
        OutputStream os;
        try {
            os = (OutputStream) hc.getOutputStream();
        } catch (IOException e) {
            return null;
        }
        try {
            os.write(stb);
            Iterator<Byte> it = vb.iterator();
            while (it.hasNext()) {
                os.write(it.next());
            }
            os.write(enb);
            os.flush();
        } catch (IOException e) {
            return null;
        }
        try {
            hc.getContent();
        } catch (IOException e) {
        }
        String moveurl = hc.getHeaderField("Location");
        if ((moveurl == null) || (moveurl.length() == 0)) {
            InputStream cc;
            try {
                cc = (InputStream) hc.getContent();
                InputStreamReader r = new InputStreamReader(cc);
                CharBuffer cb = CharBuffer.allocate(1024);
                int len = r.read(cb);
                if (len > 0) {
                    String s = new String(cb.array(), 0, len);
                    int stl = s.indexOf("<link>");
                    int enl = s.indexOf("</link>");
                    if ((stl > 0) && (enl > stl)) {
                        moveurl = s.substring(stl + 6, enl);
                    }
                }
            } catch (IOException e) {
            }
        }
        try {
            URI u = url.toURI();
            return u.resolve(moveurl).toURL();
        } catch (Exception e) {
        }
        try {
            return new URL(moveurl);
        } catch (Exception e) {
        }
        return null;
    }
}
