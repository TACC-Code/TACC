class BackupThread extends Thread {
    @Primitive
    public static Value thread_wait_timed_write(final CodeRunner ctxt, final Value fd) {
        return Threads.RESUMED_IO;
    }
}
