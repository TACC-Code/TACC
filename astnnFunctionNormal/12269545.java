class BackupThread extends Thread {
        @Override
        public LispObject execute(LispObject gate, LispObject timeout) throws ConditionThrowable {
            checkForGate(gate);
            long msecs = LispThread.javaSleepInterval(timeout);
            try {
                ((Gate) gate).waitForOpen(msecs);
                return T;
            } catch (InterruptedException e) {
                return error(new LispError("The thread " + LispThread.currentThread().writeToString() + " was interrupted."));
            }
        }
}
