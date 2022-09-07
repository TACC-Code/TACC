class BackupThread extends Thread {
    public ThreadState getThreadState(PySystemState newSystemState) {
        ThreadState ts = (ThreadState) cachedThreadState.get();
        if (ts != null) {
            return ts;
        }
        Thread t = Thread.currentThread();
        if (newSystemState == null) {
            Py.writeDebug("threadstate", "no current system state");
            newSystemState = Py.defaultSystemState;
        }
        ts = new ThreadState(t, newSystemState);
        cachedThreadState.set(ts);
        return ts;
    }
}
