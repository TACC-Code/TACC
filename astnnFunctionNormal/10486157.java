class BackupThread extends Thread {
    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        List<NodeChannel> channels = configService.getChannels();
        cleanSlate(TestConstants.TEST_PREFIX + "data_event", TestConstants.TEST_PREFIX + "data", TestConstants.TEST_PREFIX + "outgoing_batch");
        createDataEvent("Foo", triggerHistId, TestConstants.TEST_CHANNEL_ID, DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        List<OutgoingBatch> list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list != null);
        assertEquals(list.size(), 1);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        createDataEvent("Foo", triggerHistId, TestConstants.TEST_CHANNEL_ID, DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        createDataEvent("Foo", triggerHistId, TestConstants.TEST_CHANNEL_ID, DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list != null);
        assertTrue(list.size() == 2);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        assertTrue(list.get(1).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        batchService.markOutgoingBatchSent(list.get(0));
        list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list.size() == 2);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        assertTrue(list.get(1).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        batchService.setBatchStatus(list.get(0).getBatchId(), Status.OK);
        list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        OutgoingBatch ilBatch = new OutgoingBatch();
        ilBatch.setNodeId(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        ilBatch.setChannelId(TestConstants.TEST_CHANNEL_ID);
        ilBatch.setBatchType(BatchType.INITIAL_LOAD);
        batchService.insertOutgoingBatch(ilBatch);
        list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list.size() == 2);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        assertTrue(list.get(1).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
        batchService.setBatchStatus(ilBatch.getBatchId(), Status.OK);
        list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0).getChannelId().equals(TestConstants.TEST_CHANNEL_ID));
    }
}
