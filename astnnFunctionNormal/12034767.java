class BackupThread extends Thread {
    public void load(URL url) throws IOException {
        load(url.openStream());
    }
}
