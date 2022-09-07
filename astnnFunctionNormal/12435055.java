class BackupThread extends Thread {
    protected void doTestContentLength(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        long expectedContentLength = conn.getContentLength();
        if (expectedContentLength != -1) {
            byte[] buffer = new byte[2048];
            InputStream in = conn.getInputStream();
            try {
                long actualContentLength = 0;
                int length;
                while ((length = (in.read(buffer))) >= 0) {
                    actualContentLength += length;
                }
                assertEquals("Inaccurate explicit content length", expectedContentLength, actualContentLength);
            } finally {
                in.close();
            }
        }
    }
}
