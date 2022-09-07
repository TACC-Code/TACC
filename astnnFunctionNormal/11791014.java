class BackupThread extends Thread {
    public void dump(jq_RegisterState regs) {
        SystemInterface.debugwriteln(this + ": current Java thread = " + currentThread);
        StackCodeWalker.stackDump(regs.getEip(), regs.getEbp());
        for (int i = 0; i < readyQueue.length; ++i) {
            SystemInterface.debugwriteln(this + ": ready queue " + i + " = " + readyQueue[i]);
        }
        SystemInterface.debugwriteln(this + ": idle queue = " + idleQueue);
        SystemInterface.debugwriteln(this + ": transfer queue = " + transferQueue);
    }
}
