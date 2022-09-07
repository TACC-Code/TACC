class BackupThread extends Thread {
    private long checkModified(Resource r, long previousModifiedTime) {
        try {
            URL url = r.getURL();
            if (LOG.isDebugEnabled()) LOG.debug("Checking modified for url " + url);
            URLConnection c = url.openConnection();
            c.setDoInput(false);
            c.setDoOutput(false);
            long lastModified = c.getLastModified();
            if (previousModifiedTime < lastModified) {
                return lastModified;
            }
        } catch (IOException e) {
            LOG.debug("Unable to read last modified date of plugin resource" + e.getMessage(), e);
        }
        return -1;
    }
}
