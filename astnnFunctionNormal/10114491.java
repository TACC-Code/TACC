class BackupThread extends Thread {
    public void channelUserParted(ChannelUserEvent e) {
        if (e.getChannel().equals(this)) {
            ChannelUser user = e.getUser();
            if (user != null) {
                removeChannelUser(user);
            }
        }
    }
}
