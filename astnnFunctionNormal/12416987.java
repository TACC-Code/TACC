class BackupThread extends Thread {
    public void addRequest(String fileName, Document request) {
        File outFile = new File(getRequestDir(), fileName);
        if (outFile.exists()) {
            log.warn("An attempt was made to write the request file to a file that already exists");
            throw new FSServiceException(FSServiceException.Code.UNEXPECTED);
        }
        try {
            XMLUtil.transformToStream(request, new FileOutputStream(outFile));
        } catch (FileNotFoundException ex) {
            log.error("Could not write to request file", ex);
        }
        log.debug("wrote request to file " + outFile.getAbsolutePath());
    }
}
