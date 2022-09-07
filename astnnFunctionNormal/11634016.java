class BackupThread extends Thread {
    public ChannelIF getChannel() {
        if (handlers.get(this.currentHandlerId) != null) {
            return handlers.get(this.currentHandlerId).getChannel();
        } else {
            return null;
        }
    }
}
