class BackupThread extends Thread {
    public String setAlarm(int armed, int sensitivity, int inputArmed, int ioLinkage, int mail, int uploadInterval) {
        StringBuffer ret = new StringBuffer();
        try {
            URL url = new URL("http://" + host + "/set_alarm.cgi?motion_armed=" + armed + "user=" + user + "&pwd=" + password);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                ret.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            logException(e);
        }
        return ret.toString();
    }
}
