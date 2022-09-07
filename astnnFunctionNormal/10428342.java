class BackupThread extends Thread {
    private HeapHeader getHeapHeaderAndCheckPosition(final HeapHeader heapHeaderReference) throws HeapException {
        final HeapHeader heapHeader = heapElementManager.getHeapHeader();
        assertEquals("header must be at file beginning", 0, heapHeader.getPositionInFile());
        if (heapHeaderReference != null) {
            assertEquals("writed and readed must be equals", heapHeaderReference, heapHeader);
        }
        return heapHeader;
    }
}
