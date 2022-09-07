class BackupThread extends Thread {
    public static void initNativeThreads(jq_NativeThread nt, int num) {
        Assert._assert(num <= MAX_NATIVE_THREADS);
        native_threads = new jq_NativeThread[num];
        native_threads[0] = nt;
        for (int i = 1; i < num; ++i) {
            jq_NativeThread nt2 = native_threads[i] = new jq_NativeThread(i);
            nt2.thread_handle = SystemInterface.create_thread(_nativeThreadEntry.getDefaultCompiledVersion().getEntrypoint(), HeapAddress.addressOf(nt2));
            nt2.myHeapAllocator.init();
            nt2.myCodeAllocator.init();
            if (TRACE) SystemInterface.debugwriteln("Native thread " + i + " initialized");
        }
        all_native_threads_initialized = true;
    }
}
