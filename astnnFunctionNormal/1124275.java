class BackupThread extends Thread {
    public void notifyOnDoneStatus() {
        try {
            sendResponse(_metaDataSocket.getChannel(), SlaveStartupProtocol.SLAVE_DONE_PROCESSING);
            _logger.log(Level.ALL, "SLAVE_DONE_PROCESSING sent");
            System.out.println("SLAVE_DONE_PROCESSING sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
        raiseSystemEvent(EngineEvents.FINISHED_PROCESSING);
    }
}
