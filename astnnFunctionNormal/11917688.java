class BackupThread extends Thread {
    private Document getResponse(HttpGet request) throws ClientProtocolException, IOException, ParserConfigurationException, IllegalStateException, SAXException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != HTTP_STATUS_OK) throw new ClientProtocolException("The HTTP request is wrong.");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(response.getEntity().getContent());
    }
}
