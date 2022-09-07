class BackupThread extends Thread {
    @Override
    public DataChannel<?> getChannel() {
        return UpdatesDataChannel.getTagChannel(tag_name);
    }
}
