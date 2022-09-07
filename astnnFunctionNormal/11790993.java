class BackupThread extends Thread {
    public static void endCurrentJavaThread() {
        jq_Thread t = Unsafe.getThreadBlock();
        if (TRACE) Debug.writeln("Ending Java thread " + t);
        Assert._assert(!t.isThreadSwitchEnabled());
        _num_of_java_threads.getAddress().atomicSub(1);
        if (TRACE) Debug.writeln("Number of Java threads now: " + num_of_java_threads);
        if (t.isDaemon()) _num_of_daemon_threads.getAddress().atomicSub(1);
        jq_NativeThread nt = t.getNativeThread();
        Unsafe.setThreadBlock(nt.schedulerThread);
        nt.currentThread = nt.schedulerThread;
        CodeAddress ip = _schedulerLoop.getDefaultCompiledVersion().getEntrypoint();
        StackAddress fp = nt.original_ebp;
        StackAddress sp = nt.original_esp;
        if (TRACE) SystemInterface.debugwriteln("Long jumping back to schedulerLoop, ip:" + ip.stringRep() + " fp: " + fp.stringRep() + " sp: " + sp.stringRep());
        HeapAddress a = (HeapAddress) sp.offset(4).peek();
        Assert._assert(a.asObject() == nt, "arg to schedulerLoop got corrupted: " + a.stringRep() + " should be " + HeapAddress.addressOf(nt).stringRep());
        Unsafe.longJump(ip, fp, sp, 0);
        Assert.UNREACHABLE();
    }
}
