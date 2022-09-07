class BackupThread extends Thread {
    public static final String getHTTPContent(String address, final boolean dontVerify) {
        String content = "";
        OutputStream out = null;
        HttpsURLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            out = new ByteArrayOutputStream();
            conn = (HttpsURLConnection) url.openConnection();
            if (dontVerify) {
                conn.setHostnameVerifier(HostnameNonVerifier);
            }
            in = conn.getInputStream();
            final byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
            content = out.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        return content;
    }
}
