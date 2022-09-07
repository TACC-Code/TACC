class BackupThread extends Thread {
    public void markAsRead(Long id) {
        Channel channel = this.getChannel(id);
        channel.setNumberOfRead(channel.getNumberOfItems());
    }
}
