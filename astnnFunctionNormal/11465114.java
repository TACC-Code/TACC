class BackupThread extends Thread {
    private InputStream getFileAsInputStream(String file) throws BtransException {
        InputStream is;
        ClassLoader loader;
        URL url = null;
        try {
            loader = InputConverter.class.getClassLoader();
            url = loader.getResource(file);
            log.info("Loading file from " + url);
            schemeFilePath = new File(url.toURI()).getAbsolutePath();
            is = url.openStream();
            return is;
        } catch (FileNotFoundException fnfEx) {
            throw new BtransException("Cannot find file " + url.toString());
        } catch (Exception ex) {
            throw new BtransException("Cannot load file " + file + ". Reason: " + ex.getMessage());
        }
    }
}
