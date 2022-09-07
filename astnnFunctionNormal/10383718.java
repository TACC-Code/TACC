class BackupThread extends Thread {
    public void login() throws IOException {
        Date date = new Date();
        if (sid != null && date.getTime() - lastDate.getTime() < 1000 * 60 * 30) {
            return;
        }
        String postStr = "Email=" + URLEncoder.encode(ConfigureService.getInstance().getString("googleuserid"), "UTF-8") + "&Passwd=" + ConfigureService.getInstance().getString("googlepasswd");
        URL url = new URL(loginUrl + "?" + postStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] key_value = line.split("=");
            if (key_value[0].equalsIgnoreCase("sid")) {
                sid = key_value[1];
            } else if (key_value[0].equalsIgnoreCase("error")) {
            }
        }
        this.lastDate = date;
    }
}
