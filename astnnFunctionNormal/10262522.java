class BackupThread extends Thread {
    public String getLastChannel() {
        if (isInChannel()) {
            lastChannel = getChannelOwner();
        }
        return lastChannel;
    }
}
