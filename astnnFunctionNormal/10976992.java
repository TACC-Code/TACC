class BackupThread extends Thread {
    public static void removeItem(Item item) {
        items.remove(item);
        getChannel(item.getChannelId()).getItems().remove(item.getId());
        channels.removeItem(item.getId());
    }
}
