class BackupThread extends Thread {
    private void setReceivers(FilterController filterController, Collection<Receiver> receivers) throws LifecycleException {
        for (Receiver receiver : receivers) {
            Channel channel = channelManager.getChannel(receiver.getChannelId());
            if (channel == null) {
                throw new LifecycleException("Channel '" + receiver.getChannelId() + "' couldn�t be found.");
            }
            MessageConsumer messageConsumer = messageConsumerFactory.createMessageConsumer(channel, receiver.isSynchronous(), receiver.getInterval());
            messageConsumer.addMessageListener(filterController);
            filterController.addMessageConsumer(messageConsumer);
        }
    }
}
