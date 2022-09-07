class BackupThread extends Thread {
    public static String post(String address, Map<String, String> params) throws IOException {
        URL url = new URL(address);
        ByteArrayOutputStream pout = new ByteArrayOutputStream();
        for (Map.Entry<String, String> p : params.entrySet()) {
            if (pout.size() > 0) {
                pout.write('&');
            }
            pout.write(p.getKey().getBytes(OConsts.UTF8));
            pout.write('=');
            pout.write(URLEncoder.encode(p.getValue(), OConsts.UTF8).getBytes(OConsts.UTF8));
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(pout.size()));
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream cout = conn.getOutputStream();
            cout.write(pout.toByteArray());
            cout.flush();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(conn.getResponseMessage());
            }
            String contentType = conn.getHeaderField("Content-Type");
            int cp = contentType != null ? contentType.indexOf(CHARSET_MARK) : -1;
            String charset = cp >= 0 ? contentType.substring(cp + CHARSET_MARK.length()) : "ISO8859-1";
            ByteArrayOutputStream res = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            try {
                LFileCopy.copy(in, res);
            } finally {
                in.close();
            }
            return new String(res.toByteArray(), charset);
        } finally {
            conn.disconnect();
        }
    }
}
