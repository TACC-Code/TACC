class BackupThread extends Thread {
    public static boolean verifyTigerTreeHash(ThexData thexData, ManagedFile managedFile, long offset, long length) {
        ManagedFileInputStream inStream = new ManagedFileInputStream(managedFile, offset);
        int thexCalculationMode = LibraryPrefs.ThexCalculationMode.get().intValue();
        MessageDigest tigerTreeDigest = new TigerTree();
        long totalRead = 0;
        int readCount = 0;
        byte[] buffer = new byte[THEX_BLOCK_SIZE * 128];
        try {
            while (totalRead < length && (readCount = inStream.read(buffer)) != -1) {
                long start = System.currentTimeMillis();
                tigerTreeDigest.update(buffer, 0, readCount);
                totalRead += readCount;
                try {
                    long end = System.currentTimeMillis();
                    Thread.sleep((end - start) * thexCalculationMode);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Hashing file interrupted.");
                }
            }
        } catch (IOException exp) {
            return false;
        }
        byte[] hash = tigerTreeDigest.digest();
        byte[] expected = thexData.getNodeHash((int) (offset / thexData.getNodeSize()));
        boolean verifyed = Arrays.equals(hash, expected);
        return verifyed;
    }
}
