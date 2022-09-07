class BackupThread extends Thread {
    public static ChannelManager getChannelManager() {
        return context.get().getChannelManager();
    }
}
