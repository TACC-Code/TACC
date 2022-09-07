class BackupThread extends Thread {
    public static MyChannelListFrame getChannelListFrame() {
        if (frame == null) frame = new MyChannelListFrame();
        return frame;
    }
}
