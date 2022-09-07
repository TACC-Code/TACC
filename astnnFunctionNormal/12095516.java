class BackupThread extends Thread {
    public void calculateChecksum(Patch ip) {
        Patch p = (Patch) ip;
        for (int i = 0; i < 55; i++) {
            calculateChecksum(p, 23 * i + 5, 23 * i + 20, 23 * i + 21);
            p.sysex[i * 23 + 2] = ((byte) (getChannel() - 1));
        }
        calculateChecksum(p, 1265 + 5, 1265 + 63, 1265 + 64);
        p.sysex[1265 + 2] = ((byte) (getChannel() - 1));
        calculateChecksum(p, 1331 + 5, 1331 + 21, 1331 + 22);
        p.sysex[1331 + 2] = ((byte) (getChannel() - 1));
        calculateChecksum(p, 1355 + 5, 1355 + 11, 1355 + 12);
        p.sysex[1355 + 2] = ((byte) (getChannel() - 1));
        calculateChecksum(p, 1369 + 5, 1369 + 15, 1369 + 16);
        p.sysex[1369 + 2] = ((byte) (getChannel() - 1));
    }
}
