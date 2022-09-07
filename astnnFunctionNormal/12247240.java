class BackupThread extends Thread {
    public TabularData browseMessageAsTable() throws OpenDataException {
        List<MessageAlertChannel> channels = channelAwareComponent.getChannels();
        if (CollectionUtils.isBlankCollection(channels)) {
            return null;
        }
        return tabularDataWrapFromChannels(channels);
    }
}
