class BackupThread extends Thread {
    private static List<byte[]> calculateTigerTreeNodes(int nodeSize, long fileSize, InputStream inStream) throws IOException {
        int thexCalculationMode = LibraryPrefs.ThexCalculationMode.get().intValue();
        int nodeCount = (int) Math.ceil((double) fileSize / (double) nodeSize);
        List<byte[]> nodeList = new ArrayList<byte[]>(nodeCount);
        MessageDigest tigerTreeDigest = new TigerTree();
        long totalRead = 0;
        int readCount = 0;
        byte[] buffer = new byte[THEX_BLOCK_SIZE * 128];
        while (totalRead < fileSize && readCount != -1) {
            tigerTreeDigest.reset();
            int nodePos = 0;
            long start = System.currentTimeMillis();
            while (nodePos < nodeSize && (readCount = inStream.read(buffer)) != -1) {
                tigerTreeDigest.update(buffer, 0, readCount);
                nodePos += readCount;
                totalRead += readCount;
                try {
                    long end = System.currentTimeMillis();
                    Thread.sleep((end - start) * thexCalculationMode);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Hashing file interrupted.");
                }
                start = System.currentTimeMillis();
            }
            nodeList.add(tigerTreeDigest.digest());
            if (readCount == -1 && totalRead != fileSize) {
                NLogger.error(ThexCalculationWorker.class, "Hashing file failed.");
                throw new IOException("Hashing file failed.");
            }
        }
        return nodeList;
    }
}
