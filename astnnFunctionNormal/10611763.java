class BackupThread extends Thread {
    public InputStream getInputStreamFromName(String name) {
        URL url = getURLFromName(name);
        if (url != null) {
            try {
                return url.openStream();
            } catch (IOException e) {
            }
        }
        return null;
    }
}
