class BackupThread extends Thread {
    protected Channel getChannel() {
        return theChannel.getChannel();
    }
}
