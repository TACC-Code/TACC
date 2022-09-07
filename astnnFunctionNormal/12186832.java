class BackupThread extends Thread {
    public int getChannelLength() {
        if (atracChannel == null) {
            return atracFileSize;
        }
        return atracChannel.length();
    }
}
