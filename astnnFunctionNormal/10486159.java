class BackupThread extends Thread {
    @SuppressWarnings("deprecation")
    @Test
    public void testMultipleChannels() {
        List<NodeChannel> channels = configService.getChannels();
        cleanSlate(TestConstants.TEST_PREFIX + "data_event", TestConstants.TEST_PREFIX + "data", TestConstants.TEST_PREFIX + "outgoing_batch");
        createDataEvent("Foo", triggerHistId, "testchannel", DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        createDataEvent("Foo", triggerHistId, "config", DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        List<OutgoingBatch> list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertNotNull(list);
        assertEquals(list.size(), 2);
    }
}
