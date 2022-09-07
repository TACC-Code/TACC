class BackupThread extends Thread {
    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        setPatchNum(patchNum);
        sendPatch(p);
        try {
            Thread.sleep(100);
            send(new byte[] { (byte) 0xF0, (byte) 0x43, (byte) (0x10 + getChannel() - 1), (byte) 0x13, (byte) 0x41, (byte) 0x7F, (byte) 0xF7 });
            Thread.sleep(100);
            send(new byte[] { (byte) 0xF0, (byte) 0x43, (byte) (0x10 + getChannel() - 1), (byte) 0x13, (byte) 0x48, (byte) 0x7F, (byte) 0xF7 });
            Thread.sleep(100);
            send(new byte[] { (byte) 0xF0, (byte) 0x43, (byte) (0x10 + getChannel() - 1), (byte) 0x13, (byte) 0x41, (byte) 0x00, (byte) 0xF7 });
            Thread.sleep(100);
            send(new byte[] { (byte) 0xF0, (byte) 0x43, (byte) (0x10 + getChannel() - 1), (byte) 0x13, (byte) 0x48, (byte) 0x7F, (byte) 0xF7 });
        } catch (Exception e) {
            ErrorMsg.reportError("Error", "Unable to Play Patch", e);
        }
    }
}
