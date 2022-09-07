class BackupThread extends Thread {
    protected void prepareForConnecting() throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        url = new URL(requestUrl.toString());
        urlConn = (HttpURLConnection) url.openConnection();
        oauthConsumer.sign(urlConn);
    }
}
