class BackupThread extends Thread {
    public static void setDouble(RVMThread thread, int depth, int slot, double v) {
        LongWriter longWriter = new LongWriter();
        longWriter.write(thread, depth, slot, Magic.doubleAsLongBits(v));
    }
}
