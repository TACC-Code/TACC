class BackupThread extends Thread {
    protected void initEncryption() throws IOException {
        readSecurityHandler = null;
        writeSecurityHandler = null;
        try {
            readSecurityHandler = SystemSecurityHandler.createFromSt(this);
            if (readSecurityHandler != null) {
                readSecurityHandler.authenticate();
            }
        } catch (COSSecurityException e) {
            IOException ioe = new IOException(e.getMessage());
            ioe.initCause(e);
            throw ioe;
        }
        writeSecurityHandler = readSecurityHandler;
    }
}
