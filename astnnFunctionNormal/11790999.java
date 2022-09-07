class BackupThread extends Thread {
    public void yieldCurrentThread() {
        jq_Thread t1 = this.currentThread;
        if (t1 == this.schedulerThread) {
            t1.enableThreadSwitch();
            Assert._assert(!t1.isThreadSwitchEnabled());
            return;
        }
        t1.wasPreempted = false;
        if (TRACE) SystemInterface.debugwriteln("Explicit yield in native thread: " + this + " Java thread: " + t1);
        switchThread();
        Assert.UNREACHABLE();
    }
}
