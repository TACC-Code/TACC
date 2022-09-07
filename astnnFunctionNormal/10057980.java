class BackupThread extends Thread {
    protected URLConnection openConnection(URL url) throws IOException {
        URL resource = classLoader.getResource(url.getPath());
        if (resource == null) {
            throw ItemscriptError.internalError(this, "openConnection.resource.not.found", url.getPath());
        }
        return resource.openConnection();
    }
}
