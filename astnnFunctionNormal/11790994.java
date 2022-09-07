class BackupThread extends Thread {
    public void nativeThreadEntry() {
        if (this != initial_native_thread) this.pid = SystemInterface.init_thread();
        Unsafe.setThreadBlock(this.schedulerThread);
        Assert._assert(this.currentThread == this.schedulerThread);
        if (USE_INTERRUPTER_THREAD) {
            it = new jq_InterrupterThread(this);
        } else {
            SystemInterface.set_interval_timer(SystemInterface.ITIMER_VIRTUAL, 10);
        }
        StackAddress sp = StackAddress.getStackPointer();
        StackAddress fp = StackAddress.getBasePointer();
        this.original_esp = (StackAddress) sp.offset(-CodeAddress.size() - HeapAddress.size());
        this.original_ebp = fp;
        if (TRACE) SystemInterface.debugwriteln("Started native thread: " + this + " sp: " + this.original_esp.stringRep() + " fp: " + this.original_ebp.stringRep());
        this.schedulerLoop();
        Assert.UNREACHABLE();
    }
}
