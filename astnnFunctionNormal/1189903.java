class BackupThread extends Thread {
    public int getChannelPressure() {
        if (channel == null) return 0;
        return channel.getChannelPressure();
    }
}
