class BackupThread extends Thread {
    @SuppressWarnings("deprecation")
    @Test
    public void testErrorChannel() {
        IConfigurationService configService = (IConfigurationService) find(Constants.CONFIG_SERVICE);
        List<NodeChannel> channels = configService.getChannels();
        cleanSlate(TestConstants.TEST_PREFIX + "data_event", TestConstants.TEST_PREFIX + "data", TestConstants.TEST_PREFIX + "outgoing_batch");
        createDataEvent("TestTable1", triggerHistId, "testchannel", DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        createDataEvent("TestTable1", triggerHistId, "testchannel", DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        createDataEvent("TestTable2", triggerHistId, "config", DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        List<OutgoingBatch> batches = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertNotNull(batches);
        assertEquals(batches.size(), 3);
        long firstBatchId = batches.get(0).getBatchId();
        long secondBatchId = batches.get(1).getBatchId();
        long thirdBatchId = batches.get(2).getBatchId();
        ackService.ack(new BatchInfo(firstBatchId, 1));
        batches = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertNotNull(batches);
        assertEquals(batches.size(), 3);
        assertEquals(batches.get(0).getBatchId(), secondBatchId, "Channel in error should have batches last - missing new batch");
        assertEquals(batches.get(1).getBatchId(), thirdBatchId, "Channel in error should have batches last - missing error batch");
        assertEquals(batches.get(2).getBatchId(), firstBatchId, "Channel in error should have batches last - missing new batch");
    }
}
