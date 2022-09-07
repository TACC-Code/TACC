class BackupThread extends Thread {
    @Override
    public final ClientChannelListener joinedChannel(final ClientChannel channel) {
        GameContext.getClientCommunication().getChannelConteiner().addChannel(channel);
        return this;
    }
}
