class BackupThread extends Thread {
    public static void setFloat(RVMThread thread, int depth, int slot, float v) {
        IntWriter intWriter = new IntWriter();
        intWriter.write(thread, depth, slot, Magic.floatAsIntBits(v));
    }
}
