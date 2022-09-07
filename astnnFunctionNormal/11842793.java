class BackupThread extends Thread {
    public URLReader(URL url, String charset) throws IOException {
        super(openStream(url), charset);
    }
}
