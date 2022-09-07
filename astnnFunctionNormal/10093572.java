class BackupThread extends Thread {
    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(initPattern);
        write32(currentPattern);
        write32(numWaitThreads);
    }
}
