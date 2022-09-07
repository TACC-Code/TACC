class BackupThread extends Thread {
    public AChannel getChannel(int index) {
        return (AChannel) get(index);
    }
}
