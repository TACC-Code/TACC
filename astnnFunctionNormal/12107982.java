class BackupThread extends Thread {
    public static byte[] writeFileFloss(ContentName completeName, int fileLength, Random randBytes) throws IOException, NoSuchAlgorithmException {
        CCNOutputStream stockOutputStream = new CCNOutputStream(completeName, outputHandle);
        DigestOutputStream digestStreamWrapper = new DigestOutputStream(stockOutputStream, MessageDigest.getInstance("SHA1"));
        byte[] bytes = new byte[BUF_SIZE];
        int elapsed = 0;
        int nextBufSize = 0;
        boolean firstBuf = true;
        System.out.println("Writing file: " + completeName + " bytes: " + fileLength);
        final double probFlush = .3;
        while (elapsed < fileLength) {
            nextBufSize = ((fileLength - elapsed) > BUF_SIZE) ? BUF_SIZE : (fileLength - elapsed);
            randBytes.nextBytes(bytes);
            digestStreamWrapper.write(bytes, 0, nextBufSize);
            elapsed += nextBufSize;
            if (firstBuf) {
                startReader(completeName, fileLength);
                firstBuf = false;
            }
            System.out.println(completeName + " wrote " + elapsed + " out of " + fileLength + " bytes.");
            if (randBytes.nextDouble() < probFlush) {
                System.out.println("Flushing buffers.");
                digestStreamWrapper.flush();
            }
        }
        digestStreamWrapper.close();
        System.out.println("Finished writing file " + completeName);
        return digestStreamWrapper.getMessageDigest().digest();
    }
}
