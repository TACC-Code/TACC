class BackupThread extends Thread {
    private Element getRootElement(URL url) throws RestletException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(url.openStream());
            Element root = document.getDocumentElement();
            return root;
        } catch (Exception e) {
            throw new RestletException(e);
        }
    }
}
