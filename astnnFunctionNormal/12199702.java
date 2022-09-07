class BackupThread extends Thread {
    public ServerSocketCloseTask(ServerSocketContext aContext) {
        super(aContext, null, 0, aContext.getTaskQueue());
        channel = aContext.getChannel();
    }
}
