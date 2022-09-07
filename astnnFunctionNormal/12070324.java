class BackupThread extends Thread {
    ChannelServiceStats(ProfileCollector collector) {
        ProfileConsumer consumer = collector.getConsumer(ProfileCollectorImpl.CORE_CONSUMER_PREFIX + "ChannelService");
        ProfileLevel level = ProfileLevel.MAX;
        ProfileDataType type = ProfileDataType.TASK_AND_AGGREGATE;
        createChannelOp = consumer.createOperation("createChannel", type, level);
        getChannelOp = consumer.createOperation("getChannel", type, level);
    }
}
