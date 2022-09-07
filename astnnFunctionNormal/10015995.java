class BackupThread extends Thread {
    public boolean updateClientState(ClientState state) {
        if (isOurQuit(state)) {
            return true;
        } else {
            Enumeration channelNames = state.getChannelNames();
            while (channelNames.hasMoreElements()) {
                String chanName = channelNames.nextElement().toString();
                Channel channelObj = state.getChannel(chanName);
                channelObj.removeMember(user, this);
            }
            return true;
        }
    }
}
