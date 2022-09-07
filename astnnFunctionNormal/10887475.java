class BackupThread extends Thread {
    protected BinaryResource createResourceFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            BinaryResource r = new BinaryResource();
            r.setId(binaryResources.size());
            r.setApplication(applicationRef);
            r.setName(url.getFile());
            r.setMimeType("media");
            r.setOriginalUrl(url.toExternalForm());
            r.setBytes(FileUtils.readInputStreamAsBytes(url.openStream()));
            binaryResources.add(r);
            return r;
        } catch (Exception e) {
            throw new RuntimeException("Could not create resource from URL: " + urlStr, e);
        }
    }
}
