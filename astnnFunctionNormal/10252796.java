class BackupThread extends Thread {
    public void stop() throws IOException {
        if (status == Status.STARTED) {
            pause();
        }
        if (status != Status.PAUSED) {
            throw new IllegalStateException("ServerSocket is not in paused state");
        }
        getChannel().close();
        setChannel(null);
        status = Status.STOPPED;
    }
}
