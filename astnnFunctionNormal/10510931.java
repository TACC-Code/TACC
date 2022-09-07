class BackupThread extends Thread {
    public ISQLDriver[] findMissingDefaultDrivers(URL url) throws IOException, XMLException {
        ISQLDriver[] result = null;
        InputStreamReader isr = new InputStreamReader(url.openStream());
        ArrayList<ISQLDriver> missingDrivers = new ArrayList<ISQLDriver>();
        try {
            XMLObjectCache tmp = new XMLObjectCache();
            tmp.load(isr, null, true);
            for (Iterator<ISQLDriver> iter = tmp.getAllForClass(SQL_DRIVER_IMPL); iter.hasNext(); ) {
                ISQLDriver defaultDriver = iter.next();
                if (!containsDriver(defaultDriver)) {
                    missingDrivers.add(defaultDriver);
                }
            }
        } catch (DuplicateObjectException ex) {
            s_log.error("Received an unexpected DuplicateObjectException", ex);
        } finally {
            isr.close();
        }
        if (missingDrivers.size() > 0) {
            result = missingDrivers.toArray(new ISQLDriver[missingDrivers.size()]);
        }
        return result;
    }
}
