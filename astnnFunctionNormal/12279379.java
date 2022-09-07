class BackupThread extends Thread {
    public boolean updateClientState(ClientState state) {
        boolean stateChanged = false;
        Channel channelObj = state.getChannel(channel);
        if (channel == null) {
            return false;
        }
        if (channelObj == null) {
            return false;
        }
        for (String nick : names) {
            if (!channelObj.isMemberInChannel(nick)) {
                channelObj.addMember(nick, this);
                stateChanged = true;
            }
        }
        return stateChanged;
    }
}
