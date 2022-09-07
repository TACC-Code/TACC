class BackupThread extends Thread {
    public void run() {
        try {
            LOG.info("thread started");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = url.openConnection().getInputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            is.close();
            os.close();
            size = os.size();
        } catch (IOException ex) {
            LOG.error(ex);
            ex.printStackTrace();
            exception = ex;
        } finally {
            LOG.info("thread stopped");
        }
    }
}
