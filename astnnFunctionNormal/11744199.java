class BackupThread extends Thread {
    static byte[] calc(IStreamInputSource in, MessageDigest digest) throws IOException {
        ImmediateIteratorX<byte[], ? extends IOException> blocks = in.forRead().byteBlocks(true);
        byte[] block;
        try {
            while ((block = blocks.next()) != null || !blocks.isEnded()) digest.update(block);
        } catch (RuntimizedException e) {
            e.rethrow(IOException.class);
        }
        return digest.digest();
    }
}
