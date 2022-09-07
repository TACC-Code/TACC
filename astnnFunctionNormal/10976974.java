class BackupThread extends Thread {
    public static Channel getChannel(int index) {
        return visibleChannels.get(index);
    }
}
