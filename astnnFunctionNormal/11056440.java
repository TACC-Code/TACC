class BackupThread extends Thread {
    @Override
    public long transferFrom(final InputStream in, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        final long remaining = this.remaining;
        if ((remaining == 0) || (maxCount == 0)) {
            return 0;
        } else {
            maxCount = (maxCount < 0) ? remaining : Math.min(maxCount, remaining);
            final long count = IOStreams.transfer(in, ensureOpen(), maxCount);
            this.remaining = remaining - count;
            if (remaining == count) eof();
            return count;
        }
    }
}
