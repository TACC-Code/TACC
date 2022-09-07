class BackupThread extends Thread {
    protected void checkValidationFromFile(URL url) throws Exception {
        InputSource is = new InputSource(url.openStream());
        is.setSystemId(url.toExternalForm());
        checkValidation(is);
    }
}
