class BackupThread extends Thread {
    public static void suspendAllThreads() {
        if (!all_native_threads_started) {
            if (TRACE) Debug.writeln("Native threads haven't started yet.");
            return;
        }
        if (TRACE) Debug.writeln("Suspending all native threads: ", native_threads.length);
        jq_Thread t = Unsafe.getThreadBlock();
        Assert._assert(!t.isThreadSwitchEnabled());
        for (int i = 0; i < native_threads.length; ++i) {
            jq_NativeThread nt = native_threads[i];
            if (nt == t.getNativeThread()) continue;
            for (; ; ) {
                if (TRACE) Debug.writeln("Attempting to suspend native thread ", i);
                nt.suspend();
                jq_Thread t2 = nt.getCurrentJavaThread();
                if (t2.isThreadSwitchEnabled()) break;
                if (TRACE) Debug.writeln("Failed, trying again.");
                nt.resume();
                SystemInterface.msleep(0);
            }
            if (TRACE) Debug.writeln("Success!");
        }
        if (TRACE) Debug.writeln("Finished suspending native threads");
    }
}
