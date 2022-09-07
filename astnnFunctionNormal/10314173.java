class BackupThread extends Thread {
    public VM_Thread() {
        this(MM_Interface.newStack(STACK_SIZE_NORMAL >> LOG_BYTES_IN_ADDRESS));
        if (hpm_counters == null) hpm_counters = new HPM_counters();
        if (global_tid == GLOBAL_TID_INITIAL_VALUE) {
            assignGlobalTID();
            if (VM_HardwarePerformanceMonitors.hpm_trace) {
                VM_HardwarePerformanceMonitors.writeThreadToHeaderFile(global_tid, threadSlot, getClass().toString());
            }
        }
    }
}
