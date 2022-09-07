class BackupThread extends Thread {
    public Channel getChannel(int num) {
        return ((num >= 0 && num < channels.size()) ? channels.get(num) : null);
    }
}
