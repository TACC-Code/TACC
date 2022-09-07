class BackupThread extends Thread {
    public boolean updateClientState(ClientState state) {
        boolean changed = false;
        Iterator modesI = modes.iterator();
        Channel channel = state.getChannel(channelName);
        while (modesI.hasNext()) {
            Mode mode = (Mode) modesI.next();
            channel.setMode(mode);
            changed = true;
        }
        return changed;
    }
}
