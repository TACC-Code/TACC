class BackupThread extends Thread {
    public Channel getChannel(String signalName, ValueTransform transform) {
        return newChannel(signalName, transform);
    }
}
