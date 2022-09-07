class BackupThread extends Thread {
    public int getChannelCount() {
        return vorbisInfo == null ? -1 : vorbisInfo.channels;
    }
}
