class BackupThread extends Thread {
    public AceMessageInterface waitTimer(int timerid) {
        Thread thr = Thread.currentThread();
        if ((thr instanceof AceThread) == false) {
            writeErrorMessage("This method is not being called from an object which is a sub-class of type AceThread", null);
            return null;
        }
        AceThread cthread = (AceThread) thr;
        while (true) {
            AceMessageInterface msg = cthread.waitMessage();
            if ((msg instanceof AceTimerMessage) == true) {
                if (((AceTimerMessage) msg).getTimerId() == timerid) {
                    return msg;
                }
            } else if ((msg instanceof AceSignalMessage) == true) {
                return msg;
            }
        }
    }
}
