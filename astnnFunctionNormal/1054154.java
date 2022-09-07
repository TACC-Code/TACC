class BackupThread extends Thread {
    public void read(URL url) throws InfosetException, IOException {
        if (url == null) throw new IOException("no URL");
        this.url = url;
        read(new InputStreamReader(url.openStream()));
    }
}
