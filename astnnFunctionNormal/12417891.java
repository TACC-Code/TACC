class BackupThread extends Thread {
    private Document sendRequest(HttpUriRequest httpUriRequest) throws FlickrException, RemoteException, NetworkException {
        InputStream responseBodyAsStream = null;
        try {
            log.info(httpUriRequest.getMethod() + ": " + httpUriRequest.getURI());
            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            HttpEntity httpEntity = httpResponse.getEntity();
            responseBodyAsStream = httpEntity.getContent();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(responseBodyAsStream);
            checkFlickrError(doc);
            return doc;
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new NetworkException("Network communication error!", e);
        } catch (SAXException e) {
            throw new RemoteException("XML-Error in Flickr-Response! ", e);
        } finally {
            if (responseBodyAsStream != null) {
                try {
                    responseBodyAsStream.close();
                } catch (IOException e) {
                    log.warn("could not close InputStream... ", e);
                }
            }
            httpUriRequest.abort();
        }
    }
}
