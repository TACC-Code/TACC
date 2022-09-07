class BackupThread extends Thread {
    @Override
    protected AbstractSelectableChannel getChannel() throws IOException {
        int t = getTimeoutAsInt();
        if (t > 0) {
            SocketChannelWithTimeouts res = (SocketChannelWithTimeouts) SocketChannelWithTimeouts.open();
            res.setConnectTimeout(t);
            res.setReadTimeout(t);
            return res;
        } else {
            return SocketChannel.open();
        }
    }
}
