class BackupThread extends Thread {
    public void authentication() throws IOException {
        URL nexus = new URL("https://nexus.passport.com/rdr/pprdr.asp");
        logger.fine("Connected to Nexus");
        HttpURLConnection connection = (HttpURLConnection) nexus.openConnection();
        logger.fine("Nexus response: " + connection.getResponseCode());
        String s = connection.getHeaderField("PassportURLs");
        s = "https://" + s.substring(s.indexOf("DALogin=") + 8);
        s = s.substring(0, s.indexOf(','));
        logger.fine("Redirected URL: " + s);
        URL url = new URL(s);
        connection = (HttpURLConnection) url.openConnection();
        String header = "Passport1.4 OrgVerb=GET,OrgURL=http%3A%2F%2F" + "messenger%2Emsn%2Ecom,sign-in=" + username + ",pwd=" + password + "," + challenge;
        connection.setRequestProperty("Authorization", header);
        int code = connection.getResponseCode();
        logger.info("Authentication response: " + code);
        if (code == 302) {
            String location = connection.getHeaderField("Location");
            url = new URL(location);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", header);
            code = connection.getResponseCode();
        }
        switch(code) {
            case 200:
                logger.fine("Authentication Success: " + code);
                s = connection.getHeaderField("Authentication-Info");
                logger.fine("Authentication Info: " + s);
                String ticket;
                ticket = s.substring(s.indexOf('\'') + 1, s.lastIndexOf('\''));
                logger.fine("Ticket: " + ticket);
                out.print(Command.USR('S', ticket));
                out.flush();
                break;
            case 401:
                logger.severe("Authentication Error: " + code);
                Error.translateError("401");
                break;
            default:
                logger.severe("Authentication Error: " + code);
        }
        connection.disconnect();
    }
}
