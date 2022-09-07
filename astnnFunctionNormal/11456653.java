class BackupThread extends Thread {
    public void transferCancelled(LgFileTransferThread i_threadThatWasCancelled) {
        this.writeToConsole("> the transfer of " + i_threadThatWasCancelled.getSourceFile() + " was cancelled.");
    }
}
