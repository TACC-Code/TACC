class BackupThread extends Thread {
    public void transferFailed(LgFileTransferThread i_threadThatFailed, Exception i_exception) {
        this.writeToConsole("> failed to transfer " + i_threadThatFailed.getSourceFile() + ": ");
        if (i_exception instanceof ftraq.FTraqFailure) {
            this.writeToConsole(((ftraq.FTraqFailure) i_exception).getMessages());
        } else {
            this.writeToConsole("> an unexpeted exception occured: ");
            this.writeToConsole(i_exception.getClass() + ": " + i_exception.getMessage());
        }
    }
}
