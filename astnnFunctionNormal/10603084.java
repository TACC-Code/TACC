class BackupThread extends Thread {
    public void init() {
        final byte[] accessBytes = new byte[access.length * 2];
        org.pwsafe.lib.Util.newRandBytes(accessBytes);
        ShortBuffer accessShorts = ByteBuffer.wrap(accessBytes).asShortBuffer();
        for (int i = 0; i < access.length; i++) {
            access[i] = accessShorts.get(i);
        }
        try {
            tempFile = File.createTempFile("jpw", ".dat");
            LOG.info("Using temporary file " + tempFile);
            tempFile.deleteOnExit();
            outlet = new RandomAccessFile(tempFile, "rw");
            channel = outlet.getChannel();
            buffer = channel.map(MapMode.READ_WRITE, 0, BUFFER_SIZE);
            assert (buffer.isDirect());
            for (int i = 0; i < BUFFER_SIZE; i++) {
                buffer.put(org.pwsafe.lib.Util.newRand());
            }
            buffer.flip();
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }
}
