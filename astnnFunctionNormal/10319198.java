class BackupThread extends Thread {
    public void open(String url, int mode, boolean timeouts) throws IOException {
        Log.log(Log.IO, "entering http open '" + url + "' mode: " + mode);
        this.url = new URL(url);
        proxy = url.startsWith("https") ? null : ApplicationManager.getInstance().getProperty("me4se.httpproxy");
        Log.log(Log.IO, "using proxy: " + proxy);
        con = proxy == null ? (HttpURLConnection) this.url.openConnection() : (HttpURLConnection) new URL(proxy).openConnection();
        con.setUseCaches(false);
        boolean doOutput = (mode & Connector.WRITE) != 0;
        con.setDoOutput(doOutput);
        con.setDoInput(true);
        con.setRequestProperty("User-Agent", ApplicationManager.getInstance().getProperty("me4se.useragent", ApplicationManager.getInstance().getProperty("microedition.platform", "MobileRunner-J2ME")));
        if (proxy != null) {
            con.setRequestProperty("me4se-target-url", url);
        }
        Log.log(Log.IO, "leaving: http open");
    }
}
