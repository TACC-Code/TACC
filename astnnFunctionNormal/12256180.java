class BackupThread extends Thread {
    public static void setInt(RVMThread thread, int depth, int slot, int v) {
        IntWriter intWriter = new IntWriter();
        intWriter.write(thread, depth, slot, v);
    }
}
