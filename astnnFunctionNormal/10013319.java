class BackupThread extends Thread {
    public static WebStats make(URL url) throws IOException {
        URLConnection con = url.openConnection();
        int size = con.getContentLength();
        return size == -1 ? new WebStatsBasic() : new WebStatsProgress(size);
    }
}
