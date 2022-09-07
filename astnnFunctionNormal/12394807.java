class BackupThread extends Thread {
    public static final <T> T doWithLock(final File f, ExnTransformer<RandomAccessFile, T, ?> transf) throws Exception {
        RandomAccessFile out = null;
        try {
            mkParentDirs(f);
            out = new RandomAccessFile(f, "rw");
            out.getChannel().lock();
            final T res = transf.transformChecked(out);
            out.close();
            out = null;
            return res;
        } catch (final Exception e) {
            Exception toThrow = e;
            if (out != null) try {
                out.close();
            } catch (final IOException e2) {
                toThrow = ExceptionUtils.createExn(IOException.class, "couldn't close: " + e2.getMessage(), e);
            }
            throw toThrow;
        }
    }
}
