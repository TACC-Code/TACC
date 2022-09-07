class BackupThread extends Thread {
    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        setPatchNum(patchNum);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        ((Patch) p).sysex[2] = (byte) (0x30 + getChannel() - 1);
        ((Patch) p).sysex[05] = (byte) bankNum;
        try {
            send(((Patch) p).sysex);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
}
