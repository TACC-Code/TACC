class BackupThread extends Thread {
    public void transferSucceeded(LgFileTransferThread i_threadThatSucceded) {
        this.writeToConsole("> succesfully transferred " + i_threadThatSucceded.getTransferredBytes() + " bytes to " + i_threadThatSucceded.getTargetFile().getURL());
    }
}
