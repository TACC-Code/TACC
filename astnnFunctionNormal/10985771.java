class BackupThread extends Thread {
    protected void dumpScreenshot(WebDriver source, TakesScreenshot driver) throws IOException {
        Window window = source.manage().window();
        Dimension d = null;
        Point p = null;
        try {
            d = window.getSize();
            p = window.getPosition();
        } catch (Exception e) {
            if (UtilLog.LOG.isInfoEnabled()) {
                UtilLog.LOG.info("Could not get size and position.");
            }
        }
        try {
            File scrFile = driver.getScreenshotAs(OutputType.FILE);
            tmpDump = File.createTempFile("crt", getExtension(scrFile));
            tmpDump.delete();
            FileUtils.copyFile(scrFile, tmpDump);
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug("Saved page screen to temporary file " + tmpDump);
            }
            tmpSource = File.createTempFile("crt", ".html");
            tmpSource.delete();
            FileUtils.writeStringToFile(tmpSource, source.getPageSource());
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug("Saved page source to temporary file " + tmpSource);
            }
        } catch (WebDriverException e) {
            if (UtilLog.LOG.isInfoEnabled()) {
                UtilLog.LOG.info("Could not dump screenshot.");
            }
        } finally {
            if (d != null && p != null) {
                if (UtilLog.LOG.isInfoEnabled()) {
                    UtilLog.LOG.info("Restore size and location.");
                }
                window.setPosition(p);
                window.setSize(d);
            }
        }
    }
}
