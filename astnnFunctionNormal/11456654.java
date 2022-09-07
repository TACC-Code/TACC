class BackupThread extends Thread {
    public void transferStatusUpdate(LgFileTransferThread i_threadThatChanged) {
        this.writeToConsole("> " + i_threadThatChanged.getSourceFile() + ": " + i_threadThatChanged.getStatusString() + ", " + i_threadThatChanged.getTransferredBytes() + " bytes transferred");
    }
}
