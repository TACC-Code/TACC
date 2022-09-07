class BackupThread extends Thread {
    public void testCloneWriteableToReadOnly() throws Exception {
        final Directory dir1 = new MockRAMDirectory();
        TestIndexReaderReopen.createIndex(dir1, true);
        IndexReader reader = IndexReader.open(dir1, false);
        IndexReader readOnlyReader = reader.clone(true);
        if (!isReadOnly(readOnlyReader)) {
            fail("reader isn't read only");
        }
        if (deleteWorked(1, readOnlyReader)) {
            fail("deleting from the original should not have worked");
        }
        if (readOnlyReader.hasChanges) {
            fail("readOnlyReader has a write lock");
        }
        reader.close();
        readOnlyReader.close();
        dir1.close();
    }
}
