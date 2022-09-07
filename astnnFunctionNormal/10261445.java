class BackupThread extends Thread {
    private boolean doThreadReferenceCmdset(lejos.nxt.debug.PacketStream in) {
        boolean handled = false;
        PacketStream ps;
        ps = new PacketStream(this, in.id(), Packet.Reply, Packet.ReplyNoError);
        Thread thread;
        VM.VMThread vmThread;
        try {
            int threadId = in.readObjectId();
            Object obj = memGetReference(0, threadId);
            if (obj instanceof Thread) {
                thread = (Thread) obj;
            } else {
                System.out.println("No thread");
                sendErrorReply(in, INVALID_THREAD);
                return true;
            }
            if (monitor.isSystemThread(thread)) {
                System.out.println("System thread");
                sendErrorReply(in, INVALID_THREAD);
                return true;
            }
        } catch (PacketStreamException e) {
            sendErrorReply(in, INVALID_OBJECT);
            handled = true;
            return true;
        }
        switch(in.cmd()) {
            case TR_NAME:
                String name = thread.getName();
                ps.writeString(name);
                handled = true;
                break;
            case TR_SUSPEND:
                VM.suspendThread(thread);
                handled = true;
                break;
            case TR_RESUME:
                VM.resumeThread(thread);
                handled = true;
                break;
            case TR_STATUS:
                vmThread = VM.getVM().getVMThread(thread);
                int jdwpState;
                switch(vmThread.state & 0x7f) {
                    case THREAD_STATE_NEW:
                        jdwpState = JDWP_THREAD_STATUS_NOT_STARTED;
                        break;
                    case THREAD_STATE_STARTED:
                    case THREAD_STATE_RUNNING:
                        jdwpState = JDWP_THREAD_STATUS_RUNNING;
                        break;
                    case THREAD_STATE_DEAD:
                        jdwpState = JDWP_THREAD_STATUS_ZOMBIE;
                        break;
                    case THREAD_STATE_CONDVAR_WAITING:
                    case THREAD_STATE_JOIN:
                        jdwpState = JDWP_THREAD_STATUS_WAIT;
                        break;
                    case THREAD_STATE_MON_WAITING:
                    case THREAD_STATE_SYSTEM_WAITING:
                        jdwpState = JDWP_THREAD_STATUS_MONITOR;
                        break;
                    case THREAD_STATE_SLEEPING:
                        jdwpState = JDWP_THREAD_STATUS_SLEEPING;
                        break;
                    default:
                        jdwpState = JDWP_THREAD_STATUS_UNKNOWN;
                        break;
                }
                ps.writeInt(jdwpState);
                if ((vmThread.state & THREAD_STATE_SUSPENDED) != 0) {
                    ps.writeInt(1);
                } else {
                    ps.writeInt(0);
                }
                handled = true;
                break;
            case TR_FRAME_COUNT:
                vmThread = VM.getVM().getVMThread(thread);
                if ((vmThread.state & THREAD_STATE_SUSPENDED) == 0) {
                    sendErrorReply(in, THREAD_NOT_SUSPENDED);
                    return true;
                }
                ps.writeInt(vmThread.stackFrameIndex & 0xFF);
                handled = true;
                break;
            case TR_FRAMES:
                vmThread = VM.getVM().getVMThread(thread);
                if ((vmThread.state & THREAD_STATE_SUSPENDED) == 0) {
                    sendErrorReply(in, THREAD_NOT_SUSPENDED);
                    return true;
                }
                int startFrame = in.readInt();
                int length = in.readInt();
                int frameCount = vmThread.stackFrameIndex & 0xFF;
                if (startFrame >= frameCount) {
                    sendErrorReply(in, INVALID_THREAD);
                    return true;
                }
                if (startFrame + length >= frameCount) {
                    sendErrorReply(in, INVALID_THREAD);
                    return true;
                } else if (length == -1) {
                    length = frameCount - startFrame;
                }
                ps.writeInt(frameCount);
                VM.VMStackFrames frames = vmThread.getStackFrames();
                for (int i = startFrame; i < startFrame + length; i++) {
                    VM.VMStackFrame frame = frames.get(i);
                    ps.writeFrameId(i);
                    writeLocation(ps, vmThread, frame, i == 0);
                }
                handled = true;
                break;
            case TR_INTERRUPT:
                thread.interrupt();
                handled = true;
                break;
            case TR_SUSPEND_COUNT:
                vmThread = VM.getVM().getVMThread(thread);
                if ((vmThread.state & THREAD_STATE_SUSPENDED) != 0) {
                    ps.writeInt(1);
                } else {
                    ps.writeInt(0);
                }
                handled = true;
                break;
        }
        if (handled) {
            ps.send();
        }
        return handled;
    }
}
