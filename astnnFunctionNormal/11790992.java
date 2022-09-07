class BackupThread extends Thread {
    public static void startJavaThread(jq_Thread t) {
        _num_of_java_threads.getAddress().atomicAdd(1);
        if (t.isDaemon()) _num_of_daemon_threads.getAddress().atomicAdd(1);
        jq_NativeThread nt = getLeastBusyThread();
        Assert._assert(t.isThreadSwitchEnabled());
        t.disableThreadSwitch();
        CodeAddress ip = t.getRegisterState().getEip();
        if (TRACE) SystemInterface.debugwriteln("Java thread " + t + " enqueued on native thread " + nt + " ip: " + ip.stringRep() + " cc: " + CodeAllocator.getCodeContaining(ip));
        jq_Thread my_t = Unsafe.getThreadBlock();
        my_t.disableThreadSwitch();
        jq_NativeThread my_nt = my_t.getNativeThread();
        Unsafe.setThreadBlock(my_nt.schedulerThread);
        nt.transferQueue.enqueue(t);
        Unsafe.setThreadBlock(my_t);
        my_t.enableThreadSwitch();
    }
}
