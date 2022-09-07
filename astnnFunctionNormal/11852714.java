class BackupThread extends Thread {
    void generateTrace(PrintWriter writer) {
        _vm.setDebugTraceMode(_debugTraceMode);
        DataTree.Reset();
        DataTree.ResetObjectTable();
        DataTree.Init();
        EventThread eventThread = new EventThread(_vm, _excludes, writer);
        eventThread.setEventRequests();
        eventThread.start();
        redirectOutput();
        _vm.resume();
        try {
            eventThread.join();
            errThread.join();
            outThread.join();
        } catch (InterruptedException exc) {
        }
        DataTree.StartDebugging();
    }
}
