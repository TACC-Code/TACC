class BackupThread extends Thread {
    private boolean process(DebugInterface monitor, PacketStream dst) {
        if (monitor.typ != nxtEventKind) return false;
        if (countFilter == currentCount) {
            currentCount = 0;
        } else if (countFilter > 0) {
            currentCount++;
            return false;
        }
        if (threadFilter != null && monitor.thread != threadFilter) return false;
        if (nxtEventKind == DebugInterface.DBG_BREAKPOINT && methodFilter != -1 && (methodFilter != monitor.method || pcFilter != monitor.pc)) return false;
        if (nxtEventKind == DebugInterface.DBG_EXCEPTION) {
            if (exceptionFilter != null && !exceptionFilter.isInstance(monitor.exception)) return false;
            if (monitor.method2 >= 0 ? (exceptionFlags & 1) == 0 : (exceptionFlags & 2) == 0) return false;
        }
        dst.writeByte(eventKind);
        dst.writeInt(JDWPDebugServer.getObjectAddress(this));
        switch(eventKind) {
            case JDWPConstants.EVENT_VM_INIT:
            case JDWPConstants.EVENT_THREAD_DEATH:
            case JDWPConstants.EVENT_THREAD_START:
                dst.writeObjectId(JDWPDebugServer.getObjectAddress(monitor.thread));
                break;
            case JDWPConstants.EVENT_SINGLE_STEP:
                System.out.println("Write step event");
            case JDWPConstants.EVENT_BREAKPOINT:
                dst.writeObjectId(JDWPDebugServer.getObjectAddress(monitor.thread));
                JDWPDebugServer.writeLocation(dst, VM.getVM().getMethod(monitor.method), monitor.pc);
                break;
            case JDWPConstants.EVENT_EXCEPTION:
                dst.writeObjectId(JDWPDebugServer.getObjectAddress(monitor.thread));
                JDWPDebugServer.writeLocation(dst, VM.getVM().getMethod(monitor.method), monitor.pc);
                dst.writeByte(JDWPConstants.OBJECT_TAG);
                dst.writeObjectId(JDWPDebugServer.getObjectAddress(monitor.exception));
                if (monitor.method2 >= 0) JDWPDebugServer.writeLocation(dst, VM.getVM().getMethod(monitor.method2), monitor.pc2); else JDWPDebugServer.writeNoLocation(dst);
                break;
        }
        if (eventKind == JDWPConstants.EVENT_SINGLE_STEP) {
            requestStepInformation(JDWPDebugServer.instance);
        }
        return true;
    }
}
