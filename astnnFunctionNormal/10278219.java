class BackupThread extends Thread {
    @Override
    public InputStream getStream(String request) throws Exception {
        URL url = new URL(getAbsolute(request));
        return url.openStream();
    }
}
