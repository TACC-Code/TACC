class BackupThread extends Thread {
    public static int updateChannel(Channel channel) throws SAXException, IOException {
        Channel newChannel = ChannelFactory.getChannelFromXML(channel.getChannelURL());
        if (channel.getIconPath() == null) {
            String iconPath;
            try {
                iconPath = ChannelFactory.extractIconPath(channel.getLink());
                channel.setIconPath(iconPath);
            } catch (IOException ioe) {
            }
        }
        channel.setTitle(newChannel.getTitle());
        channel.setLink(newChannel.getLink());
        channel.setDescription(newChannel.getDescription());
        channel.setImageURL(newChannel.getImageURL());
        channel.setImageTitle(newChannel.getImageTitle());
        channel.setImageLink(newChannel.getImageLink());
        int newItemsCount = 0;
        for (Item updatedItem : ChannelFactory.getDownloadedItems()) {
            boolean itemAlreadyExists = false;
            for (Item item : channels.getItems(channel.getId())) {
                if (updatedItem.equals(item)) {
                    itemAlreadyExists = true;
                    break;
                }
            }
            if (!itemAlreadyExists) {
                channels.addItem(updatedItem);
                channel.addItem(updatedItem.getId());
                updateItemsList(updatedItem);
                newItemsCount++;
            }
        }
        if (updateUnreadItemsCount(channel)) {
            channels.write();
        }
        return newItemsCount;
    }
}
