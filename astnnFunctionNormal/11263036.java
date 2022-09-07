class BackupThread extends Thread {
    public void indexURL(String url) throws Exception {
        InputStream _in;
        if (MiscUtilities.isURL(url)) _in = new URL(url).openStream(); else {
            _in = new FileInputStream(url);
            url = "file:" + url;
        }
        indexStream(_in, url);
    }
}
