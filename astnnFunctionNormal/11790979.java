class BackupThread extends Thread {
    public static void startNativeThreads() {
        for (int i = 1; i < native_threads.length; ++i) {
            if (TRACE) SystemInterface.debugwriteln("Native thread " + i + " started");
            native_threads[i].resume();
        }
        all_native_threads_started = true;
    }
}
