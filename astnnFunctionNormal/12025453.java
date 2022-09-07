class BackupThread extends Thread {
    public XmlStreamReader(URL url) throws IOException {
        this(url.openConnection(), null);
    }
}
