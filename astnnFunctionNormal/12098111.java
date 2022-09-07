class BackupThread extends Thread {
    public OTFServerQuad getQuad(String id, OTFNetWriterFactory writers) throws RemoteException {
        if (writers != null) throw new RemoteException("writers need to be NULL, when reading from file");
        if (this.id == null) readQuad();
        if (id != null && !id.equals(this.id)) throw new RemoteException("id does not match, set id to NULL will match ALL!");
        return quad;
    }
}
