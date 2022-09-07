class BackupThread extends Thread {
    public ChannelFilter(ReadPort<Tin> readPort, WritePort<Tout> writePort, ChannelOperation<Tin, Tout> filter, boolean propogatePoison) {
        if (readPort == null) {
            throw new IllegalArgumentException("Read port may not be null.");
        }
        if (writePort == null) {
            throw new IllegalArgumentException("Write port may not be null.");
        }
        if (filter == null) {
            throw new IllegalArgumentException("Filter operation may not be null.");
        }
        this.readPort = readPort;
        this.writePort = writePort;
        this.filter = filter;
        this.propogatePoison = propogatePoison;
    }
}
