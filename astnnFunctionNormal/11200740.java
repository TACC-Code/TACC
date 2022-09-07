class BackupThread extends Thread {
    public static BufferedReader sendGetRequest(String endpoint, String requestParameters) {
        BufferedReader result = null;
        try {
            Properties sysProperties = System.getProperties();
            sysProperties.put("proxyHost", Constants.PROXY);
            sysProperties.put("proxyPort", Constants.PROXYPORT);
            sysProperties.put("proxySet", Constants.PROXYENABLED);
            Authenticator.setDefault(new MyAuthenticator());
            String urlStr = endpoint;
            if (requestParameters != null && requestParameters.length() > 0) {
                urlStr += "?" + requestParameters;
            }
            java.net.URL url = new java.net.URL(urlStr);
            java.lang.System.out.println(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                result = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8000);
            } else {
                System.out.println("Failed with a " + conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
