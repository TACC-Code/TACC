class BackupThread extends Thread {
    @Override
    SocketChannel getChannel() {
        return ch;
    }
}
