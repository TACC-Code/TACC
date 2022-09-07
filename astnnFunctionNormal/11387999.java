class BackupThread extends Thread {
    public final java.nio.channels.SocketChannel getChannelTcp() {
        switch(this.type) {
            case TYPE_TCP:
                return this.tcp.getChannel();
            default:
                throw new alto.sys.Error.Bug();
        }
    }
}
