class BackupThread extends Thread {
    SelectableChannel getChannel() {
        return channel;
    }
}
