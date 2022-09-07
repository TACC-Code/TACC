class BackupThread extends Thread {
    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[296];
            sysex[0] = (byte) 0xF0;
            sysex[1] = (byte) 0x00;
            sysex[2] = (byte) 0x20;
            sysex[3] = (byte) 0x29;
            sysex[4] = (byte) 0x01;
            sysex[5] = (byte) 0x21;
            sysex[6] = (byte) (getChannel() - 1);
            sysex[7] = (byte) 0x00;
            sysex[8] = (byte) 0x09;
            sysex[295] = (byte) 0xF7;
            System.arraycopy(bank.sysex, getPatchStart(patchNum), sysex, 9, 296 - 9);
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            Logger.reportError("Error", "Error in Nova1 Bank Driver", e);
            return null;
        }
    }
}
