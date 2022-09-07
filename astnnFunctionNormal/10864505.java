class BackupThread extends Thread {
    public void calculateChecksum(Patch p) {
        calculateChecksum(p, 6, 38, 39);
        calculateChecksum(p, 47, 139, 140);
        ((Patch) p).sysex[43] = ((byte) (getChannel() - 1));
    }
}
