class BackupThread extends Thread {
    public HttpEntity getFriendListXml() throws IOException, FriendListRetrievalException {
        HttpGet getReq = new HttpGet("https://secure.eu.playstation.com/ajax/mypsn/friend/presence/");
        HttpResponse response = httpClient.execute(getReq, context);
        if (response.getStatusLine().getStatusCode() == 200) {
            return response.getEntity();
        }
        response.getEntity().consumeContent();
        throw new FriendListRetrievalException("Can not retrieve friend list without logging in.");
    }
}
