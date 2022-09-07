class BackupThread extends Thread {
    public boolean open() throws HL7IOException {
        if (isOpen()) return true;
        if (isWriter()) {
            return openWriter();
        } else if (isReader()) {
            return openReader();
        }
        throw new HL7IOException("Not a reader. Not a writer.", HL7IOException.INCONSISTENT_STATE);
    }
}
