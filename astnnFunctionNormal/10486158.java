class BackupThread extends Thread {
    @SuppressWarnings("deprecation")
    @Test
    public void testBatchBoundary() {
        List<NodeChannel> channels = configService.getChannels();
        cleanSlate(TestConstants.TEST_PREFIX + "data_event", TestConstants.TEST_PREFIX + "data", TestConstants.TEST_PREFIX + "outgoing_batch");
        int size = 50;
        int count = 3;
        if (!getDbDialect().supportsOpenCursorsAcrossCommit()) {
            count = 1;
        }
        assertTrue(count <= size);
        for (int i = 0; i < size * count; i++) {
            createDataEvent("Foo", triggerHistId, TestConstants.TEST_CHANNEL_ID, DataEventType.INSERT, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        }
        for (int i = 0; i < count; i++) {
            batchService.buildOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID, channels);
        }
        List<OutgoingBatch> list = batchService.getOutgoingBatches(TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertNotNull(list);
        assertEquals(list.size(), count);
        for (int i = 0; i < count; i++) {
            assertTrue(getBatchSize(list.get(i).getBatchId()) <= size + 1);
        }
    }
}
