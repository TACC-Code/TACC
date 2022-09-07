class BackupThread extends Thread {
    static void writeLocation(lejos.nxt.debug.PacketStream ps, VM.VMThread thread, VM.VMStackFrame frame, boolean isTopFrame) {
        VM.VMMethod method = frame.getVMMethod();
        int pc = frame.pc - (method.codeOffset & 0xFFFF) - VM.getVM().getImage().address;
        if (!isTopFrame && method.signature != 3) {
            pc -= 2;
        }
        writeLocation(ps, method, pc);
    }
}
