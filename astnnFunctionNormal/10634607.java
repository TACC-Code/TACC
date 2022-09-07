class BackupThread extends Thread {
    public String getTokenInfo(String token, String tokenSecret) throws Exception {
        GoogleOAuthParameters params = new GoogleOAuthParameters();
        params.setOAuthConsumerKey(consumerKey);
        params.setOAuthConsumerSecret(consumerSecret);
        params.setOAuthToken(token);
        params.setOAuthTokenSecret(tokenSecret);
        URL url = new URL("https://www.google.com/accounts/AuthSubTokenInfo");
        String header = helper.getAuthorizationHeader(url.toString(), "GET", params);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", header);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
