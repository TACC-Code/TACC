class BackupThread extends Thread {
    public SocketChannel getFeederReplicaChannel() {
        return feederReplicaChannel.getChannel();
    }
}
