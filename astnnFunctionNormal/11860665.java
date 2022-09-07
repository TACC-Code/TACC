class BackupThread extends Thread {
    @Override
    public void setReadWrite(boolean readWrite) {
        this.readwrite = readWrite;
        if (readWrite) getComponent().setStyle("cursor: pointer; border: 1px solid;"); else getComponent().setStyle("cursor: default; border: none;");
    }
}
