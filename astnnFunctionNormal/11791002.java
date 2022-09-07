class BackupThread extends Thread {
    private void switchThread(jq_Thread t2) {
        jq_Thread t1 = this.currentThread;
        Unsafe.setThreadBlock(this.schedulerThread);
        this.currentThread = this.schedulerThread;
        CodeAddress ip = (CodeAddress) StackAddress.getBasePointer().offset(StackAddress.size()).peek();
        if (TRACE) SystemInterface.debugwriteln("Thread switch in native thread: " + this + " Java thread: " + t1 + " ip: " + ip.stringRep() + " cc: " + CodeAllocator.getCodeContaining(ip));
        if (t1.isThreadSwitchEnabled()) {
            SystemInterface.debugwriteln("Java thread " + t1 + " has thread switching enabled on threadSwitch entry!");
            SystemInterface.die(-1);
        }
        Assert._assert(t1 != this.schedulerThread);
        jq_RegisterState state = t1.getRegisterState();
        state.setEip((CodeAddress) state.getEsp().peek());
        state.setEsp((StackAddress) state.getEsp().offset(StackAddress.size() + CodeAddress.size()));
        if (t1 != t2) {
            for (int i = 0; ; ++i) {
                if (i == readyQueue.length) {
                    Assert.UNREACHABLE();
                    return;
                }
                boolean exists = readyQueue[i].remove(t2);
                if (exists) break;
            }
            if (t2.wasPreempted && !t2.isDaemon()) --preempted_thread_counter;
        }
        transferExtraWork();
        if (t1 != t2) {
            ip = t2.getRegisterState().getEip();
            if (TRACE) SystemInterface.debugwriteln("New ready Java thread: " + t2 + " ip: " + ip.stringRep() + " cc: " + CodeAllocator.getCodeContaining(ip));
            int priority = t1.getPriority();
            readyQueue[priority].enqueue(t1);
            if (t1.wasPreempted && !t1.isDaemon()) ++preempted_thread_counter;
            Assert._assert(!t2.isThreadSwitchEnabled());
        }
        currentThread = t2;
        SystemInterface.set_current_context(t2, t2.getRegisterState());
        Assert.UNREACHABLE();
    }
}
