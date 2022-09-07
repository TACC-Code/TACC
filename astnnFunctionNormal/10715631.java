class BackupThread extends Thread {
    public void handleQxEvent(Event evt) {
        if (evt.getCmd() instanceof BlinkMCmd) {
            BlinkMCmd blinkMCmd = (BlinkMCmd) evt.getCmd();
            System.out.println(CmdUtil.INSTANCE.getCmdInfo(evt.getCmd()));
            if (evt.getKind() == EVENT_KIND.TX_CMD_REMOVED || evt.getKind() == EVENT_KIND.RX_CMD_REMOVED) {
                Object obj = evt.getQx().getEngine().getPort().getChannel();
                if (obj instanceof Serial) {
                    synchronized (evt.getQx().getEngine().getOutputInterpreter()) {
                        byte[] frame = evt.getQx().getEngine().getOutputInterpreter().cmd2ByteArray(blinkMCmd);
                        ((Serial) obj).write(frame);
                        if (blinkMCmd.getFlag() == FLAG.READ || blinkMCmd.getFlag() == FLAG.READWRITE) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            byte[] ret = new byte[blinkMCmd.getRetValues().length];
                            ((Serial) obj).readBytes(ret);
                            evt.getQx().getEngine().getOutputInterpreter().processResults(blinkMCmd, ret);
                        }
                    }
                }
            }
        }
    }
}
