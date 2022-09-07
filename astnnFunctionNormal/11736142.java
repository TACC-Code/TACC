class BackupThread extends Thread {
    private byte[] doSend(URL url, byte[] message) throws Throwable {
        URLConnection conn = null;
        DataOutputStream out = null;
        InputStream in = null;
        try {
            conn = url.openConnection();
            conn.setUseCaches(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_OCTET_STREAM);
            conn.setRequestProperty(CONTENT_LENGTH, "" + message.length);
            out = new DataOutputStream(conn.getOutputStream());
            out.write(message);
            out.flush();
            in = conn.getInputStream();
            byte[] responseMessage = ByteUtil.getBytes(in);
            return responseMessage;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Throwable ignore) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Throwable ignore) {
            }
        }
    }
}
