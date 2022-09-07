class BackupThread extends Thread {
    @Primitive
    public static Value thread_wait_write(final CodeRunner ctxt, final Value fd) {
        return Value.UNIT;
    }
}
