class BackupThread extends Thread {
    protected void playPatch(Patch p) {
        try {
            sendPatch(p);
            Thread.sleep(100);
            send((0x90 + (getChannel() - 1)), 36, 127);
            Thread.sleep(100);
            send((0x80 + (getChannel() - 1)), 36, 0);
            send((0x90 + (getChannel() - 1)), 42, 127);
            Thread.sleep(100);
            send((0x80 + (getChannel() - 1)), 42, 0);
            send((0x90 + (getChannel() - 1)), 38, 127);
            Thread.sleep(100);
            send((0x80 + (getChannel() - 1)), 38, 0);
            send((0x90 + (getChannel() - 1)), 46, 127);
            Thread.sleep(100);
            send((0x80 + (getChannel() - 1)), 46, 0);
        } catch (Exception e) {
            ErrorMsg.reportError("Error", "Unable to Play Drums", e);
        }
    }
}
