class BackupThread extends Thread {
    public Patch createNewPatch() {
        byte[] sysex = new byte[(297 * 128)];
        byte[] sysexHeader = new byte[10];
        sysexHeader[0] = (byte) 0xF0;
        sysexHeader[1] = (byte) 0x00;
        sysexHeader[2] = (byte) 0x20;
        sysexHeader[3] = (byte) 0x29;
        sysexHeader[4] = (byte) 0x01;
        sysexHeader[5] = (byte) 0x21;
        sysexHeader[6] = (byte) (getChannel() - 1);
        sysexHeader[7] = (byte) 0x02;
        sysexHeader[8] = (byte) (0x05);
        sysexHeader[9] = (byte) 0x00;
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < 128; i++) {
            sysexHeader[9] = (byte) i;
            System.arraycopy(sysexHeader, 0, p.sysex, i * 297, 10);
            System.arraycopy(NovationNova1InitPatch.initpatch, 9, p.sysex, (i * 297) + 10, 296 - 9);
            p.sysex[(297 * i) + 296] = (byte) 0xF7;
        }
        calculateChecksum(p);
        return p;
    }
}
