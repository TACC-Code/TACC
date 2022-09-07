class BackupThread extends Thread {
    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(bufSize);
        write32(freeSize);
        write32(numSendWaitThreads);
        write32(numReceiveWaitThreads);
    }
}
