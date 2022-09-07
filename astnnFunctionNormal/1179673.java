class BackupThread extends Thread {
    @Override
    public void unRegister() {
        AppContext.getDataManager().removeObject(AppContext.getChannelManager().getChannel(chatPrefix + getName()));
        super.unRegister();
    }
}
