class BackupThread extends Thread {
    public void save(ILocator pLocator, Map options) throws IOException {
        if (options == null) {
            options = new HashMap();
        }
        if ((pLocator != null) && (pLocator != getLocator())) {
            replaceLocator(pLocator);
        }
        boolean incremental = true;
        EnumWriteMode writeMode = doc.getWriteModeHint();
        doc.setWriteModeHint(EnumWriteMode.UNDEFINED);
        if (writeMode.isUndefined()) {
            Object tempHint = options.get(OPTION_WRITEMODEHINT);
            if (tempHint instanceof EnumWriteMode) {
                writeMode = (EnumWriteMode) tempHint;
            }
        }
        if (writeMode.isFull()) {
            incremental = false;
        }
        IRandomAccess tempRandomAccess = getRandomAccess();
        if (tempRandomAccess == null) {
            throw new IOException("nowhere to write to");
        }
        if (tempRandomAccess.isReadOnly()) {
            throw new FileNotFoundException("destination is read only");
        }
        COSWriter writer = new COSWriter(tempRandomAccess, getWriteSecurityHandler());
        writer.setIncremental(incremental);
        writer.writeDocument(this);
        readSecurityHandler = writeSecurityHandler;
    }
}
