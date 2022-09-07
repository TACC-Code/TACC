class BackupThread extends Thread {
    public short getChannelId() throws RemoteException {
        return ids[DataTestApplet.channel];
    }
}
