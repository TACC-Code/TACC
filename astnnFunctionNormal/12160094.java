class BackupThread extends Thread {
    private PlatformConfig read(URI location) throws IOException {
        URL url;
        try {
            url = location.toURL();
        } catch (MalformedURLException mue) {
            IllegalArgumentException failure = new IllegalArgumentException("Failed to convert URI to URL");
            failure.initCause(mue);
            throw failure;
        }
        InputStream input = url.openStream();
        try {
            XMLDocument document = (XMLDocument) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, input);
            PlatformConfig platformConfig = (PlatformConfig) AdvertisementFactory.newAdvertisement(document);
            return platformConfig;
        } finally {
            input.close();
        }
    }
}
