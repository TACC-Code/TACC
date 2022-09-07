class BackupThread extends Thread {
    public static void initInitialNativeThread() {
        initial_native_thread.pid = SystemInterface.init_thread();
        Unsafe.setThreadBlock(initial_native_thread.schedulerThread);
        initial_native_thread.thread_handle = SystemInterface.get_current_thread_handle();
        initial_native_thread.myHeapAllocator.init();
        initial_native_thread.myCodeAllocator.init();
        if (TRACE) SystemInterface.debugwriteln("Initial native thread initialized");
    }
}
