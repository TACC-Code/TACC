class BackupThread extends Thread {
    public Patch createNewPatch() {
        byte[] sysex = new byte[35 * 852 + 8];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x42;
        sysex[2] = (byte) (0x30 + getChannel() - 1);
        sysex[3] = (byte) 0x28;
        sysex[4] = (byte) 0x4C;
        sysex[5] = (byte) 0x00;
        sysex[35 * 852 + 7] = (byte) 0xF7;
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < 35; i++) setPatchName(p, i, "New Patch");
        calculateChecksum(p);
        return p;
    }
}
