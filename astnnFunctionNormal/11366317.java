class BackupThread extends Thread {
    static int getChannelShift(int mask) {
        if (mask == 0) return 0;
        int i;
        for (i = 0; ((mask & 1) == 0) && (i < 32); ++i) {
            mask >>>= 1;
        }
        return i;
    }
}
