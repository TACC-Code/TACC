class BackupThread extends Thread {
    public long transferFrom(InputStream in, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        long count = 0;
        if (maxCount < 0) {
            for (int b; (b = in.read()) >= 0; ) {
                write(b);
                count++;
            }
        } else {
            for (int b; (count < maxCount) && ((b = in.read()) >= 0); ) {
                write(b);
                count++;
            }
        }
        return count;
    }
}
