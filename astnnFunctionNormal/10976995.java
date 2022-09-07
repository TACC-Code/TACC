class BackupThread extends Thread {
    public static boolean search(boolean caseSensitive, boolean titlesOnly, int scope, String needle) {
        List<Item> haystack = new LinkedList<Item>();
        List<Item> matchingItems = new LinkedList<Item>();
        String itemContents = "";
        if (scope == 0) {
            haystack.addAll(items);
        } else if (scope == 1) {
            for (Channel channel : visibleChannels) {
                haystack.addAll(channels.getItems(channel.getId()));
            }
        } else if (scope == 2) {
            for (Channel channel : channels.getChannels()) {
                haystack.addAll(channels.getItems(channel.getId()));
            }
        }
        for (Item item : haystack) {
            itemContents = item.getTitle();
            if (!titlesOnly) {
                if (item.getDescription() != null) {
                    itemContents = itemContents.concat(item.getDescription());
                }
            }
            if (!caseSensitive) {
                itemContents = itemContents.toLowerCase();
            }
            if (itemContents.contains(needle)) {
                matchingItems.add(item);
            }
        }
        if (matchingItems.size() > 0) {
            items = matchingItems;
            return true;
        }
        return false;
    }
}
