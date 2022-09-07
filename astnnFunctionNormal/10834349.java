class BackupThread extends Thread {
    public void setBankNum(int bankNum) {
        try {
            programMode[2] = (byte) (0x30 + (getChannel() - 1));
            send(programMode);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            send(0xB0 + (getChannel() - 1), 0x00, 0x00);
            send(0xB0 + (getChannel() - 1), 0x20, 0x00 + bankNum);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }
}
