class BackupThread extends Thread {
    public String getStatus() {
        StringBuffer ret = new StringBuffer();
        try {
            URL url = new URL("http://" + host + "/get_status.cgi?user=" + user + "&pwd=" + password);
            LOG.debug("getStatus " + url);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                ret.append(inputLine);
            }
            in.close();
            LOG.debug(ret.indexOf("var id"));
            if (ret.indexOf("var id") != -1) {
                ret = new StringBuffer("connected");
            } else {
            }
        } catch (Exception e) {
            ret.append(e.getMessage());
            logException(e);
        }
        return ret.toString();
    }
}
