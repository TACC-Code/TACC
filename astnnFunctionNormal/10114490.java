class BackupThread extends Thread {
    public void channelUserJoined(ChannelUserEvent e) {
        if (e.getChannel().equals(this)) {
            addChannelUser(e.getUser());
        }
    }
}
