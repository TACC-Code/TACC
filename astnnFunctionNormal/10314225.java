class BackupThread extends Thread {
    public void dump(int verbosity) {
        VM_Scheduler.writeDecimal(getIndex());
        if (isDaemon) VM_Scheduler.writeString("-daemon");
        if (isNativeIdleThread) VM_Scheduler.writeString("-nativeidle");
        if (isIdleThread) VM_Scheduler.writeString("-idle");
        if (isGCThread) VM_Scheduler.writeString("-collector");
        if (isNativeDaemonThread) VM_Scheduler.writeString("-nativeDaemon");
        if (beingDispatched) VM_Scheduler.writeString("-being_dispatched");
    }
}
