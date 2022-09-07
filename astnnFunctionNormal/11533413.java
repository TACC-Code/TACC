class BackupThread extends Thread {
    public boolean contains(final String channelID) {
        final Iterator it = channels.iterator();
        while (it.hasNext()) {
            Channel ch = (Channel) it.next();
            if (channelID.equals(ch.getChannelID())) {
                return true;
            }
        }
        return false;
    }
}
