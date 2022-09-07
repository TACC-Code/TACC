class BackupThread extends Thread {
    public static boolean nextUnread(int tabIndex) {
        Item nextUnreadItem = new Item("", "");
        Date beginningOfTime = new Date(0);
        Date endOfTime = new Date();
        try {
            endOfTime = new SimpleDateFormat("yyyy").parse("9999");
        } catch (ParseException pe) {
        }
        if (config.getSortByNewest()) {
            nextUnreadItem.setDate(beginningOfTime);
            for (Channel channel : visibleChannels) {
                if (channel.getUnreadItemsCount() > 0) {
                    for (Item item : channels.getItems(channel.getId())) {
                        if (!item.isRead()) {
                            if (item.getDate().after(nextUnreadItem.getDate())) {
                                nextUnreadItem = item;
                            }
                        }
                    }
                }
            }
        } else {
            nextUnreadItem.setDate(endOfTime);
            for (Channel channel : visibleChannels) {
                if (channel.getUnreadItemsCount() > 0) {
                    for (Item item : channels.getItems(channel.getId())) {
                        if (!item.isRead()) {
                            if (item.getDate().before(nextUnreadItem.getDate())) {
                                nextUnreadItem = item;
                            }
                        }
                    }
                }
            }
        }
        if (nextUnreadItem.getDate().equals(beginningOfTime) || nextUnreadItem.getDate().equals(endOfTime)) {
            return false;
        }
        nextUnreadItem.markAsRead();
        updateUnreadItemsCount(channels.getChannel(nextUnreadItem.getChannelId()));
        previewTabs.get(tabIndex).setCurrent(new Preview(nextUnreadItem));
        channels.write();
        return true;
    }
}
