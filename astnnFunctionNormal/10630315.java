class BackupThread extends Thread {
    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[852 + 9];
            sysex[00] = (byte) 0xF0;
            sysex[01] = (byte) 0x42;
            sysex[2] = (byte) (0x30 + getChannel() - 1);
            sysex[03] = (byte) 0x28;
            sysex[04] = (byte) 0x40;
            sysex[05] = (byte) 0x00;
            sysex[06] = (byte) patchNum;
            sysex[852 + 8] = (byte) 0xF7;
            System.arraycopy(bank.sysex, getPatchStart(patchNum), sysex, 7, 852);
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            Logger.reportError("Error", "Error in Wavestation Bank Driver", e);
            return null;
        }
    }
}
