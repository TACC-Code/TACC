class BackupThread extends Thread {
    static int processEvent(DebugInterface monitor, PacketStream dst) {
        Thread thread = monitor.thread;
        int sp = 0;
        int cnt = 0;
        dst.writeByte(0);
        dst.writeInt(0);
        System.out.println("Event " + monitor.typ);
        if (monitor.typ == DebugInterface.DBG_PROGRAM_EXIT) {
            cnt++;
            dst.writeByte(JDWPConstants.EVENT_VM_DEATH);
            dst.writeInt(0);
        } else if (monitor.typ == JDWPDebugServer.DBG_PROGRAM_START) {
            cnt++;
            dst.writeByte(JDWPConstants.EVENT_VM_INIT);
            dst.writeInt(0);
            dst.writeObjectId(JDWPDebugServer.getObjectAddress(thread));
            sp = SUSPEND_ALL;
        }
        synchronized (queueHead) {
            for (EventRequest req = queueHead.next; req != queueHead; req = req.next) {
                if (req.process(monitor, dst)) {
                    cnt++;
                    if (req.suspendPolicy > sp) {
                        sp = req.suspendPolicy;
                    }
                }
            }
        }
        monitor.clearEvent();
        if (cnt != 0) {
            byte[] hdr = new byte[5];
            hdr[0] = (byte) sp;
            hdr[1] = (byte) ((cnt >>> 24) & 0xff);
            hdr[2] = (byte) ((cnt >>> 16) & 0xff);
            hdr[3] = (byte) ((cnt >>> 8) & 0xff);
            hdr[4] = (byte) ((cnt >>> 0) & 0xff);
            dst.send(hdr);
            System.out.println("Event sent");
        }
        if (sp < 2) {
            monitor.resumeProgram();
        }
        if (sp == 1) {
            VM.suspendThread(thread);
        }
        return sp;
    }
}
