class BackupThread extends Thread {
    public void getFromURL(URL url) throws IOException {
        load(url.openStream());
    }
}
