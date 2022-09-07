class BackupThread extends Thread {
    public static String getRemoteIP() throws IOException {
        URL url = new URL(Conf.getInstance().getExternalIpServiceURL());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HLog.netlogger.debug("fetching remote IP");
        String res = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        conn.disconnect();
        return res;
    }
}
