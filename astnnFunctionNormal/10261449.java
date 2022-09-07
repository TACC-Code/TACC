class BackupThread extends Thread {
    private boolean doVirtualMachineCmdset(lejos.nxt.debug.PacketStream in) {
        boolean handled = false;
        PacketStream ps;
        ps = new PacketStream(this, in.id(), Packet.Reply, Packet.ReplyNoError);
        switch(in.cmd()) {
            case VM_ALL_THREADS:
                ArrayList<Thread> threads = new ArrayList<Thread>();
                for (VM.VMThread th : VM.getVM().getVMThreads()) if (!monitor.isSystemThread(th.getJavaThread())) threads.add(th.getJavaThread());
                ps.writeInt(threads.size());
                for (Thread th : threads) {
                    ps.writeObjectId(getObjectAddress(th));
                }
                handled = true;
                break;
            case VM_DISPOSE:
                ps.send();
                exit(false);
                return true;
            case VM_EXIT:
                while (!monitor.notifyEvent(DebugInterface.DBG_PROGRAM_EXIT, null)) Thread.yield();
                handled = true;
                break;
            case VM_SUSPEND:
                monitor.suspendProgram();
                handled = true;
                break;
            case VM_RESUME:
                monitor.resumeProgram();
                handled = true;
                break;
        }
        if (handled) ps.send();
        return handled;
    }
}
