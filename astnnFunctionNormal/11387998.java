class BackupThread extends Thread {
    public java.nio.channels.ReadableByteChannel getChannelReadable() {
        switch(this.type) {
            case TYPE_TCP:
                return this.tcp.getChannel();
            case TYPE_UDP:
                return this.udp.getChannel();
            case TYPE_MDP:
                return this.mdp.getChannel();
            case TYPE_SHM:
                return this.shm.getChannel();
            default:
                throw new alto.sys.Error.Bug();
        }
    }
}
