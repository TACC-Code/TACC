class BackupThread extends Thread {
    public static long createHash(byte[] data) {
        long h = 0;
        byte[] res;
        synchronized (digestFunction) {
            res = digestFunction.digest(data);
        }
        for (int i = 0; i < 4; i++) {
            h <<= 8;
            h |= ((int) res[i]) & 0xFF;
        }
        return h;
    }
}
