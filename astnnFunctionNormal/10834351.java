class BackupThread extends Thread {
    protected void sendPatch(Patch p) {
        byte[] pd = new byte[188 + 6];
        pd[0] = (byte) 0xF0;
        pd[1] = (byte) 0x42;
        pd[2] = (byte) ((byte) 0x30 + (byte) (getChannel() - 1));
        pd[3] = (byte) 0x35;
        pd[4] = (byte) 0x40;
        pd[193] = (byte) 0xF7;
        int j = 0;
        for (int i = 0; i < 164; ) {
            byte b7 = (byte) 0x00;
            for (int k = 0; k < 7; k++) {
                if (i + k < 164) {
                    b7 += (p.sysex[i + k + EXTRA_HEADER] & 128) >> (7 - k);
                    pd[j + k + 1 + 5] = (byte) (p.sysex[i + k + EXTRA_HEADER] & (byte) 0x7F);
                }
            }
            pd[j + 5] = b7;
            j += 8;
            i += 7;
        }
        try {
            send(pd);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
}
