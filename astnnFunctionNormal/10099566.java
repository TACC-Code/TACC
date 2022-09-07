class BackupThread extends Thread {
    public Workbench(URL url) throws Exception {
        this(loadCrawler(url.openStream()));
    }
}
