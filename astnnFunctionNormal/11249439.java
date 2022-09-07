class BackupThread extends Thread {
    public static final void changeByteEndianess(byte[] b, int offset, int length) {
        byte tmp;
        for (int i = offset; i < offset + length; i += 2) {
            tmp = b[i];
            b[i] = b[i + 1];
            b[i + 1] = tmp;
        }
    }
}
