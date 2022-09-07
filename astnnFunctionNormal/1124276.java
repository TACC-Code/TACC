class BackupThread extends Thread {
    public void notifyOnEvent(EngineEvents event) {
        if (event != EngineEvents.CYCLE_DETECTION_GRAPH_ANALYSIS_FINISHED) {
            return;
        }
        int pendingCallbacks = _pendingCallbacks.decrementAndGet();
        if (pendingCallbacks > 0) {
            return;
        }
        try {
            sendResponse(_metaDataSocket.getChannel(), SlaveStartupProtocol.SLAVE_CYCLE_ANALYSIS_DONE);
            _logger.log(Level.ALL, "SLAVE_CYCLE_ANALYSIS_DONE sent");
            System.out.println("SLAVE_CYCLE_ANALYSIS_DONE sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
