class BackupThread extends Thread {
    public void restoreDefaultDrivers() {
        XMLObjectCache tmpCache = new XMLObjectCache();
        URL url = URLUtil.getResourceURL("default_drivers.xml");
        try {
            InputStreamReader isr = new InputStreamReader(url.openStream());
            try {
                tmpCache.load(isr);
            } finally {
                isr.close();
            }
        } catch (Exception ex) {
            SQLExplorerPlugin.error("Error loading default driver file", ex);
        }
        Iterator it = tmpCache.getAllForClass(SQL_DRIVER_IMPL);
        while (it.hasNext()) {
            ISQLDriver driver = (ISQLDriver) it.next();
            if (driver != null) {
                try {
                    _cache.add(driver);
                    _driverMgr.registerSQLDriver(driver);
                } catch (Exception e) {
                    SQLExplorerPlugin.error("Error restoring default driver: " + driver.getName(), e);
                }
            }
        }
    }
}
