class BackupThread extends Thread {
    public UrlResourceContent(URL url, String entryName) {
        this.entryName = entryName;
        try {
            con = url.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
