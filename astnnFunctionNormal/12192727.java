class BackupThread extends Thread {
    public void channelUpdate(ChannelEvent event) {
        Channel source = event.getChannel();
        if (source == myElementsChannel) {
            List<?> oldList = (List<?>) event.getOldValue();
            List<?> newList = (List<?>) event.getNewValue();
            int oldSize = (oldList != null ? oldList.size() : 0);
            int newSize = (newList != null ? newList.size() : 0);
            if (newSize < oldSize) {
                fireIntervalRemoved(this, newSize, oldSize - 1);
                if (newSize != 0) {
                    fireContentsChanged(this, 0, newSize - 1);
                }
            } else if (oldSize < newSize) {
                fireIntervalAdded(this, oldSize, newSize - 1);
                if (oldSize != 0) {
                    fireContentsChanged(this, 0, oldSize - 1);
                }
            } else {
                if (newSize != 0) {
                    fireContentsChanged(this, 0, newSize - 1);
                }
            }
        }
    }
}
