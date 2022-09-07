class BackupThread extends Thread {
    public void testCloneReadOnlyToWriteable() throws Exception {
        final Directory dir1 = new MockRAMDirectory();
        TestIndexReaderReopen.createIndex(dir1, true);
        IndexReader reader1 = IndexReader.open(dir1, true);
        IndexReader reader2 = reader1.clone(false);
        if (isReadOnly(reader2)) {
            fail("reader should not be read only");
        }
        assertFalse("deleting from the original reader should not have worked", deleteWorked(1, reader1));
        if (reader2.hasChanges) {
            fail("cloned reader should not have write lock");
        }
        assertTrue("deleting from the cloned reader should have worked", deleteWorked(1, reader2));
        reader1.close();
        reader2.close();
        dir1.close();
    }
}
