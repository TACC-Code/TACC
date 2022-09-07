class BackupThread extends Thread {
    protected void checkValidationFromFile(String relativePath) throws Exception {
        URL url = getClass().getResource(relativePath);
        InputSource is = new InputSource(url.openStream());
        is.setSystemId(url.toExternalForm());
        checkValidation(is);
    }
}
