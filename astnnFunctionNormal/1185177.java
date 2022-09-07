class BackupThread extends Thread {
    public boolean pollForEvents() {
        numKilledInJava = 0;
        VM_Thread thread = head;
        int readCount = 0, writeCount = 0, exceptCount = 0;
        while (thread != null) {
            if (isKilled(thread)) {
                thread.throwInterruptWhenScheduled = true;
                ++numKilledInJava;
            }
            if (numKilledInJava == 0) {
                thread.waitData.accept(myDowncaster);
                VM_ThreadIOWaitData waitData = myDowncaster.waitData;
                if (VM.VerifyAssertions) VM._assert(waitData == thread.waitData);
                if (waitData.readFds != null) {
                    waitData.readOffset = readCount;
                    readCount += addFileDescriptors(allFds, READ_OFFSET + readCount, waitData.readFds);
                }
                if (waitData.writeFds != null) {
                    waitData.writeOffset = writeCount;
                    writeCount += addFileDescriptors(allFds, WRITE_OFFSET + writeCount, waitData.writeFds);
                }
                if (waitData.exceptFds != null) {
                    waitData.exceptOffset = exceptCount;
                    exceptCount += addFileDescriptors(allFds, EXCEPT_OFFSET + exceptCount, waitData.exceptFds);
                }
            }
            thread = thread.next;
        }
        if (numKilledInJava > 0) return true;
        VM_Processor.getCurrentProcessor().isInSelect = true;
        VM_BootRecord bootRecord = VM_BootRecord.the_boot_record;
        selectInProgressMutex.lock();
        int ret = VM_SysCall.call_I_A_I_I_I(bootRecord.sysNetSelectIP, VM_Magic.objectAsAddress(allFds), readCount, writeCount, exceptCount);
        selectInProgressMutex.unlock();
        VM_Processor.getCurrentProcessor().isInSelect = false;
        if (ret == -1) {
            return false;
        }
        return true;
    }
}
