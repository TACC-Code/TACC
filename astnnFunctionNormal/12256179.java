class BackupThread extends Thread {
    public static void setObject(RVMThread thread, int depth, int slot, Object v) {
        ObjectWriter objWriter = new ObjectWriter();
        objWriter.write(thread, depth, slot, v);
    }
}
