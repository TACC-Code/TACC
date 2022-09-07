class BackupThread extends Thread {
    private URL getValidURL(String systemId) {
        InputStream stream = null;
        URL url = null;
        try {
            url = new URL(systemId);
            stream = url.openStream();
            stream.close();
        } catch (MalformedURLException e) {
            url = null;
        } catch (IOException e) {
            url = null;
        } finally {
            stream = null;
        }
        return url;
    }
}
