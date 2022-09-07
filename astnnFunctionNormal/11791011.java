class BackupThread extends Thread {
    public static void initBreakThread() {
        break_nthread = new jq_NativeThread(-1);
        Thread t = new Thread("_break_");
        break_jthread = ThreadUtils.getJQThread(t);
        break_jthread.disableThreadSwitch();
        break_jthread.setNativeThread(break_nthread);
        if (TRACE) SystemInterface.debugwriteln("Break thread initialized");
    }
}
