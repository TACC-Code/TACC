class BackupThread extends Thread {
    private static void updateItemsList(Item item) {
        if (currentFilter.equals("unread")) {
            if (!item.isRead()) {
                items.add(item);
            }
        } else if (currentFilter.equals("all")) {
            items.add(item);
        } else {
            if (currentFilter.equals(item.getChannelId())) {
                items.add(item);
            }
        }
        Collections.sort(items, itemComparator);
    }
}
