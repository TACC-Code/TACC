class BackupThread extends Thread {
    public int getChannelPosition() {
        if (atracChannel == null) {
            return -1;
        }
        return (int) atracChannel.getPosition();
    }
}
