class BackupThread extends Thread {
    static int getChannelWidth(int mask, int shift) {
        if (mask == 0) return 0;
        int i;
        mask >>>= shift;
        for (i = shift; ((mask & 1) != 0) && (i < 32); ++i) {
            mask >>>= 1;
        }
        return i - shift;
    }
}
