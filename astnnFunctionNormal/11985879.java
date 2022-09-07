class BackupThread extends Thread {
    protected void playPatch(Patch p) {
        byte sysex[] = new byte[patchSize];
        System.arraycopy(((Patch) p).sysex, 0, sysex, 0, patchSize);
        sysex[PATCH_NUM_OFFSET] = 99;
        Patch p2 = new Patch(sysex);
        try {
            Thread.sleep(50);
            sendPatch(p2);
            Thread.sleep(50);
            send(0xC0 + getChannel() - 1, 99);
            Thread.sleep(50);
            super.playPatch(p2);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
}
