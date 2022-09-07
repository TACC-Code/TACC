class BackupThread extends Thread {
    public static void setLong(RVMThread thread, int depth, int slot, long v) {
        LongWriter longWriter = new LongWriter();
        longWriter.write(thread, depth, slot, v);
    }
}
