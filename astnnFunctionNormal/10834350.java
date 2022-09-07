class BackupThread extends Thread {
    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        setPatchNum(patchNum);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        sendPatchWorker(p);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        try {
            programWriteRequest[2] = (byte) (0x30 + (getChannel() - 1));
            programWriteRequest[5] = (byte) bankNum;
            programWriteRequest[6] = (byte) patchNum;
            send(programWriteRequest);
        } catch (Exception e) {
            Logger.reportError("Error", "Error with patch storing", e);
        }
    }
}
