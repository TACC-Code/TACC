class BackupThread extends Thread {
    public static final void changeWordEndianess(byte[] b, int offset, int length) {
        byte tmp;
        for (int i = offset; i < offset + length; i += 4) {
            tmp = b[i];
            b[i] = b[i + 3];
            b[i + 3] = tmp;
            tmp = b[i + 1];
            b[i + 1] = b[i + 2];
            b[i + 2] = tmp;
        }
    }
}
