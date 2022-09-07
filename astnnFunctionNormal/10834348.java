class BackupThread extends Thread {
    public void setPatchNum(int patchNum) {
        try {
            programMode[2] = (byte) (0x30 + (getChannel() - 1));
            send(programMode);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            programEditMode[2] = (byte) (0x30 + (getChannel() - 1));
            send(0xC0 + (getChannel() - 1), patchNum, 0xF7);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            send(programEditMode);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }
}
