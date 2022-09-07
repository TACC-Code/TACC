class BackupThread extends Thread {
    public static void selectItem(Item item, int tabIndex) {
        item.markAsRead();
        previewTabs.get(tabIndex).setCurrent(new Preview(item));
        if (updateUnreadItemsCount(channels.getChannel(item.getChannelId()))) {
            channels.write();
        }
    }
}
