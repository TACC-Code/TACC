class BackupThread extends Thread {
    protected InputStream getInputStream() {
        try {
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
