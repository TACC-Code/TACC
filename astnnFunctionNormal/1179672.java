class BackupThread extends Thread {
    @Override
    public ManagedReference<? extends PlayableFaction> register() {
        super.register();
        AppContext.getChannelManager().createChannel(chatPrefix + getName(), new ChatChannelListener(), Delivery.UNRELIABLE);
        return AppContext.getDataManager().createReference(this);
    }
}
