class BackupThread extends Thread {
    public void schedulerLoop() {
        HeapAddress a = (HeapAddress) this.original_esp.offset(4).peek();
        Assert._assert(a.asObject() == this);
        Assert._assert(Unsafe.getThreadBlock() == this.schedulerThread);
        for (; ; ) {
            if (this == initial_native_thread && num_of_daemon_threads == num_of_java_threads) break;
            Assert._assert(currentThread == schedulerThread);
            jq_Thread t = getNextReadyThread();
            if (t == null) {
                if (TRACE) SystemInterface.debugwriteln("Native thread " + this + " is idle!");
                SystemInterface.yield();
            } else {
                Assert._assert(!t.isThreadSwitchEnabled());
                if (TRACE) SystemInterface.debugwriteln("Native thread " + this + " scheduler loop: switching to Java thread " + t);
                currentThread = t;
                SystemInterface.set_current_context(t, t.getRegisterState());
                Assert.UNREACHABLE();
            }
        }
        dumpStatistics();
        SystemInterface.die(0);
        Assert.UNREACHABLE();
    }
}
