class BackupThread extends Thread {
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        if (ourSeedling == null) {
            String message = ("No Seedling is running to service URL " + url.toExternalForm());
            throw new IOException(message);
        }
        String path = url.getPath();
        URL resourceUrl = ourSeedling.getUrlResources().getResourceUrl(path);
        if (resourceUrl == null) {
            String message = ("Could not find resource with URL " + url.toExternalForm());
            throw new FileNotFoundException(message);
        }
        return resourceUrl.openConnection();
    }
}
