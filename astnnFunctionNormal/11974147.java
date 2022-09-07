class BackupThread extends Thread {
    private String getURLData(String urlString, String userId, String password) throws MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        if (!urlString.endsWith("?wsdl") && urlString.indexOf("jbossws/services") <= 0) {
            urlString += "/jbossws/services";
        }
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (urlString.endsWith("?wsdl")) {
            if (userId != null) {
                StringBuilder userPwd = new StringBuilder(userId);
                if (password != null) {
                    userPwd.append(':').append(password);
                }
                String encoding = new sun.misc.BASE64Encoder().encode(userPwd.toString().getBytes());
                urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
            }
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        if (log.isDebugEnabled()) {
            log.debug("Html Data :" + sb.toString());
        }
        in.close();
        return sb.toString();
    }
}
