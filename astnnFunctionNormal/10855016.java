class BackupThread extends Thread {
    public AnimChannel getChannel(int index) {
        return channels.get(index);
    }
}
