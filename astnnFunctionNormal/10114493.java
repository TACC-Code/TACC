class BackupThread extends Thread {
    public void channelUserKicked(ChannelUserEvent e) {
        if (e.getChannel().equals(this)) {
            ChannelUser user = e.getUser();
            removeChannelUser(user);
            if (user.getNick().equals(nick) && isAutoRejoinOn()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                isRunning = false;
                server.getChannels().remove(this);
                join();
            } else if (user.getNick().equals(nick)) {
                isRunning = false;
                server.getChannels().remove(this);
            }
        }
    }
}
