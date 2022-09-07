class BackupThread extends Thread {
    public DocumentReader(URL url) throws IOException {
        fInput = new InputSource(url.openStream());
    }
}
