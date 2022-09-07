class BackupThread extends Thread {
    public void storePatch(Patch p, int bankNum, int patchNum) {
        p.sysex[2] = (byte) (0x30 + getChannel() - 1);
        p.sysex[5] = (byte) bankNum;
        sendPatchWorker(p);
    }
}
