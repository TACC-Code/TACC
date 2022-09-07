class BackupThread extends Thread {
    public static byte[] get(final String _url) throws Exception {
        URL url = new URL(_url);
        URLConnection connection = url.openConnection();
        int len = connection.getContentLength();
        InputStream is = connection.getInputStream();
        byte[] bytes = null;
        if (len > 0) {
            bytes = new byte[len];
            int bytesRead = is.read(bytes, 0, len);
            int count = bytesRead;
            while (bytesRead != -1 && count < len) {
                bytesRead = is.read(bytes, count, (len - count));
                count += bytesRead;
            }
        } else {
            int b;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((b = is.read()) != -1) {
                bos.write(b);
            }
            bytes = bos.toByteArray();
        }
        is.close();
        return bytes;
    }
}
