class BackupThread extends Thread {
    public final java.nio.channels.DatagramChannel getChannelUdp() {
        switch(this.type) {
            case TYPE_UDP:
                return this.udp.getChannel();
            case TYPE_MDP:
                return this.mdp.getChannel();
            default:
                throw new alto.sys.Error.Bug();
        }
    }
}
