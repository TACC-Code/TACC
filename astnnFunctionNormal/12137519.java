class BackupThread extends Thread {
    public void forwardMessage(IRCMessageEvent e, String channel) {
        if (e == null) {
            return;
        }
        AbstractIRCChannel[] chans;
        if (channel == null || channel.equals("")) {
            chans = getChannels();
        } else {
            AbstractIRCChannel chan = getChannel(channel);
            if (chan == null) {
                chan = addChannel(channel);
                if (chan == null) {
                    return;
                }
            }
            chans = new AbstractIRCChannel[] { chan };
        }
        for (AbstractIRCChannel c : chans) {
            c.messageReceived(e);
        }
    }
}
