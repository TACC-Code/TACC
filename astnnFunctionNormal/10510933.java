class BackupThread extends Thread {
    public void loadDefaultDrivers(URL url) throws IOException, XMLException {
        InputStreamReader isr = new InputStreamReader(url.openStream());
        try {
            _cache.load(isr, null, true);
        } catch (DuplicateObjectException ex) {
            s_log.error("Received an unexpected DuplicateObjectException", ex);
        } finally {
            isr.close();
        }
    }
}
