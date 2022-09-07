class BackupThread extends Thread {
    public int sceKernelCancelSema(int semaid, int newcount, int numWaitThreadAddr) {
        Memory mem = Memory.getInstance();
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelSema semaid=0x" + Integer.toHexString(semaid) + " newcount=" + newcount + " numWaitThreadAddr=0x" + Integer.toHexString(numWaitThreadAddr));
        }
        if (newcount <= 0 && newcount != -1) {
            return ERROR_KERNEL_ILLEGAL_COUNT;
        }
        SceUidManager.checkUidPurpose(semaid, "ThreadMan-sema", true);
        SceKernelSemaInfo sema = semaMap.get(semaid);
        if (sema == null) {
            log.warn("sceKernelCancelSema - unknown uid 0x" + Integer.toHexString(semaid));
            return ERROR_KERNEL_NOT_FOUND_SEMAPHORE;
        }
        if (Memory.isAddressGood(numWaitThreadAddr)) {
            mem.write32(numWaitThreadAddr, sema.numWaitThreads);
        }
        sema.numWaitThreads = 0;
        if (newcount == -1) {
            sema.currentCount = sema.initCount;
        } else {
            sema.currentCount = newcount;
        }
        onSemaphoreCancelled(semaid);
        return 0;
    }
}
