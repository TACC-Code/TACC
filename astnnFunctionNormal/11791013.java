class BackupThread extends Thread {
    public static void resumeAllThreads() {
        if (!all_native_threads_started) {
            if (TRACE) Debug.writeln("Native threads haven't started yet.");
            return;
        }
        if (TRACE) Debug.writeln("Resuming all native threads");
        jq_Thread t = Unsafe.getThreadBlock();
        Assert._assert(!t.isThreadSwitchEnabled());
        for (int i = 0; i < native_threads.length; ++i) {
            jq_NativeThread nt = native_threads[i];
            if (nt == t.getNativeThread()) continue;
            if (TRACE) Debug.writeln("Resuming native thread ", i);
            nt.resume();
        }
        if (TRACE) Debug.writeln("All native threads resumed");
    }
}
