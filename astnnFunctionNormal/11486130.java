class BackupThread extends Thread {
    @Test
    public void testIncomingBatch() throws Exception {
        String[] insertValues = new String[TEST_COLUMNS.length];
        insertValues[2] = insertValues[4] = "incoming test";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CsvWriter writer = getWriter(out);
        writer.writeRecord(new String[] { CsvConstants.NODEID, TestConstants.TEST_CLIENT_EXTERNAL_ID });
        writer.writeRecord(new String[] { CsvConstants.CHANNEL, TestConstants.TEST_CHANNEL_ID });
        String nextBatchId = getNextBatchId();
        writer.writeRecord(new String[] { CsvConstants.BATCH, nextBatchId });
        writeTable(writer, TEST_TABLE, TEST_KEYS, TEST_COLUMNS);
        insertValues[0] = getNextId();
        writer.write(CsvConstants.INSERT);
        writer.writeRecord(insertValues, true);
        writer.writeRecord(new String[] { CsvConstants.COMMIT, nextBatchId });
        writer.close();
        load(out);
        IncomingBatch batch = getIncomingBatchService().findIncomingBatch(batchId, TestConstants.TEST_CLIENT_EXTERNAL_ID);
        assertEquals(batch.getStatus(), IncomingBatch.Status.OK, "Wrong status. " + printDatabase());
        assertEquals(batch.getChannelId(), TestConstants.TEST_CHANNEL_ID, "Wrong channel. " + printDatabase());
    }
}
