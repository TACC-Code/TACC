class BackupThread extends Thread {
    public void fillBlock() throws LimitReachedException {
        if (++count > ((1L << 32) - 1)) throw new LimitReachedException();
        Arrays.fill(buffer, (byte) 0x00);
        int limit = salt.length;
        in = new byte[limit + 4];
        System.arraycopy(salt, 0, in, 0, salt.length);
        in[limit++] = (byte) (count >>> 24);
        in[limit++] = (byte) (count >>> 16);
        in[limit++] = (byte) (count >>> 8);
        in[limit] = (byte) count;
        for (int i = 0; i < iterationCount; i++) {
            mac.reset();
            mac.update(in, 0, in.length);
            in = mac.digest();
            for (int j = 0; j < buffer.length; j++) buffer[j] ^= in[j];
        }
    }
}
