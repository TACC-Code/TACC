class BackupThread extends Thread {
    BufferedReader getStream(String qry) throws Exception {
        try {
            String path = statusPath + "?" + qry;
            URL url = new URL("http", webServerHost, webServerPort, path);
            URLConnection urlc = url.openConnection();
            BufferedReader is = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            return is;
        } catch (IOException e) {
            log.info("Can't connect to jkstatus " + webServerHost + ":" + webServerPort + " " + e.toString());
            return null;
        }
    }
}
