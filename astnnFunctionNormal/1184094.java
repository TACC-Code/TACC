class BackupThread extends Thread {
    public static boolean delete(File file) {
        if (file.exists()) {
            SecureRandom random = new SecureRandom();
            Trivium tri = new Trivium();
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel channel = raf.getChannel();
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
                byte[] key = new byte[10];
                byte[] nonce = new byte[10];
                random.nextBytes(key);
                random.nextBytes(nonce);
                tri.setupKey(Trivium.MODE_DECRYPT, key, 0);
                tri.setupNonce(nonce, 0);
                int buffersize = 1024;
                byte[] bytes = new byte[1024];
                while (buffer.hasRemaining()) {
                    int max = buffer.limit() - buffer.position();
                    if (max > buffersize) max = buffersize;
                    tri.process(bytes, 0, bytes, 0, max);
                    buffer.put(bytes, 0, max);
                }
                buffer.force();
                buffer.rewind();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "FileNotFoundException", e);
            } catch (IOException e) {
                Log.d(TAG, "IOException", e);
            } catch (ESJException e) {
                Log.d(TAG, "ESJException", e);
            }
            return file.delete();
        }
        return false;
    }
}
