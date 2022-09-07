class BackupThread extends Thread {
    public short getChannel() throws RemoteException {
        return DataTestApplet.channel;
    }
}
