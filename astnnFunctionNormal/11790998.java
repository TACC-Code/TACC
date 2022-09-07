class BackupThread extends Thread {
    public void threadSwitch() {
        jq_Thread t1 = this.currentThread;
        t1.wasPreempted = true;
        if (TRACE) SystemInterface.debugwriteln("Timer interrupt in native thread: " + this + " Java thread: " + t1);
        switchThread();
        Assert.UNREACHABLE();
    }
}
