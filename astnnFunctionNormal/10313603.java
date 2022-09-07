class BackupThread extends Thread {
    protected IChannelService getChannelService() {
        return XtotoApplication.get().getCometdService();
    }
}
