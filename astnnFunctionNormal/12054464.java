class BackupThread extends Thread {
    public boolean updateClientState(ClientState state) {
        if (user.equals(state.getNick())) {
            state.removeChannel(channel);
            return true;
        } else {
            Channel chanObj = state.getChannel(channel);
            chanObj.removeMember(user, this);
            return true;
        }
    }
}
