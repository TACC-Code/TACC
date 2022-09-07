class BackupThread extends Thread {
    protected String getResourceAsString(String name) throws Exception {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            is = new BufferedInputStream(this.getClass().getResourceAsStream(name));
            byte[] buf = new byte[4096];
            int nread = -1;
            while ((nread = is.read(buf)) != -1) {
                os.write(buf, 0, nread);
            }
            os.flush();
        } finally {
            os.close();
            is.close();
        }
        return os.toString();
    }
}
