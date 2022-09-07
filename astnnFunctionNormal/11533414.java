class BackupThread extends Thread {
    public void remove(final String channelID) {
        final Iterator it = channels.iterator();
        while (it.hasNext()) {
            Channel ch = (Channel) it.next();
            if (channelID.equals(ch.getChannelID())) {
                it.remove();
            }
        }
    }
}
