class BackupThread extends Thread {
    public Source resolve(String href, String base) {
        assert href != null;
        URL url;
        try {
            url = ResourceUtils.getRealPath(href, context);
        } catch (FileNotFoundException e) {
            LOG.error("File not found '" + href + "'", e);
            return null;
        } catch (MalformedURLException e) {
            LOG.error("Error creating URL '" + href + "'", e);
            return null;
        }
        String urlPath = url.toString();
        String systemId = urlPath.substring(0, urlPath.lastIndexOf('/') + 1);
        StreamSource s = null;
        try {
            s = new StreamSource(url.openStream(), systemId);
        } catch (IOException e) {
            LOG.error("Can't load the resource from '" + href + "'", e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("href:" + href + " base:" + base + " resolved path:" + s.getSystemId());
        }
        return s;
    }
}
