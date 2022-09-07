class BackupThread extends Thread {
    private void loadDefaultDrivers() {
        final URL url = URLUtil.getResourceURL("default_drivers.xml");
        try {
            InputStreamReader isr = new InputStreamReader(url.openStream());
            try {
                _cache.load(isr);
            } finally {
                isr.close();
            }
        } catch (Exception ex) {
            SQLExplorerPlugin.error("Error loading default driver file", ex);
        }
    }
}
