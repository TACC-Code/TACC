class BackupThread extends Thread {
    void reset(ZStream z, long[] c) {
        if (c != null) {
            c[0] = check;
        }
        if (mode == BTREE || mode == DTREE) {
            blens = null;
        }
        if (mode == CODES) {
            codes.free(z);
        }
        mode = TYPE;
        bitk = 0;
        bitb = 0;
        read = write = 0;
        if (checkfn != null) {
            z.adler = check = z._adler.adler32(0L, null, 0, 0);
        }
    }
}
