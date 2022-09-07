class BackupThread extends Thread {
    private int addTimer(long abs_time, AceThread cthread, long user_parm) {
        Thread calling_thread;
        if (cthread != null) {
            calling_thread = cthread;
        } else {
            calling_thread = Thread.currentThread();
        }
        if ((calling_thread instanceof AceThread) == false) {
            writeErrorMessage("This method is not being called from an object which is a sub-class of type AceThread", null);
            return -1;
        }
        int timer_id = nextTimerId++;
        AceTimerMessage msg = new AceTimerMessage(abs_time, (AceThread) calling_thread, timer_id, user_parm);
        if (insertElementInTimerQueue(msg) == true) {
            this.interrupt();
        } else {
            timer_id = -1;
        }
        return timer_id;
    }
}
