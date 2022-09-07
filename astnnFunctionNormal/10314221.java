class BackupThread extends Thread {
    public VM_Thread(int[] stack) {
        this.stack = stack;
        chosenProcessorId = (VM.runningVM ? VM_Processor.getCurrentProcessorId() : 0);
        suspendLock = new VM_ProcessorLock();
        contextRegisters = new VM_Registers();
        hardwareExceptionRegisters = new VM_Registers();
        if (!VM.runningVM) {
            VM_Scheduler.threads[threadSlot = VM_Scheduler.PRIMORDIAL_THREAD_INDEX] = this;
            VM_Scheduler.numActiveThreads += 1;
            return;
        }
        if (trace) VM_Scheduler.trace("VM_Thread", "create");
        stackLimit = VM_Magic.objectAsAddress(stack).add(STACK_SIZE_GUARD);
        INSTRUCTION[] instructions = VM_Entrypoints.threadStartoffMethod.getCurrentInstructions();
        VM.disableGC();
        VM_Address ip = VM_Magic.objectAsAddress(instructions);
        VM_Address sp = VM_Magic.objectAsAddress(stack).add(stack.length << LOG_BYTES_IN_ADDRESS);
        VM_Address fp = STACKFRAME_SENTINEL_FP;
        sp = sp.sub(STACKFRAME_HEADER_SIZE);
        fp = sp.sub(BYTES_IN_ADDRESS + STACKFRAME_BODY_OFFSET);
        VM_Magic.setCallerFramePointer(fp, STACKFRAME_SENTINEL_FP);
        VM_Magic.setCompiledMethodID(fp, INVISIBLE_METHOD_ID);
        sp = sp.sub(BYTES_IN_ADDRESS);
        contextRegisters.gprs.set(ESP, sp);
        contextRegisters.gprs.set(VM_BaselineConstants.JTOC, VM_Magic.objectAsAddress(VM_Magic.getJTOC()));
        contextRegisters.fp = fp;
        contextRegisters.ip = ip;
        int INITIAL_FRAME_SIZE = STACKFRAME_HEADER_SIZE;
        fp = VM_Memory.alignDown(sp.sub(INITIAL_FRAME_SIZE), STACKFRAME_ALIGNMENT);
        VM_Magic.setMemoryAddress(fp.add(STACKFRAME_FRAME_POINTER_OFFSET), STACKFRAME_SENTINEL_FP);
        VM_Magic.setMemoryAddress(fp.add(STACKFRAME_NEXT_INSTRUCTION_OFFSET), ip);
        VM_Magic.setMemoryInt(fp.add(STACKFRAME_METHOD_ID_OFFSET), INVISIBLE_METHOD_ID);
        contextRegisters.gprs.set(FRAME_POINTER, fp);
        contextRegisters.ip = ip;
        VM_Scheduler.threadCreationMutex.lock();
        assignThreadSlot();
        if (hpm_counters == null) hpm_counters = new HPM_counters();
        assignGlobalTID();
        if (VM_HardwarePerformanceMonitors.hpm_trace) {
            VM_HardwarePerformanceMonitors.writeThreadToHeaderFile(global_tid, threadSlot, getClass().toString());
        }
        VM_Scheduler.threadCreationMutex.unlock();
        contextRegisters.gprs.set(THREAD_ID_REGISTER, VM_Word.fromInt(getLockingId()));
        VM.enableGC();
        if (VM.runningVM) jniEnv = new VM_JNIEnvironment(threadSlot);
        onStackReplacementEvent = new OSR_OnStackReplacementEvent();
    }
}
