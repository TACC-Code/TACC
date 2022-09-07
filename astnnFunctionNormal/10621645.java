class BackupThread extends Thread {
    public static void bsw32(byte[] ary, int offset) {
        byte t = ary[offset];
        ary[offset] = ary[offset + 3];
        ary[offset + 3] = t;
        t = ary[offset + 1];
        ary[offset + 1] = ary[offset + 2];
        ary[offset + 2] = t;
    }
}
