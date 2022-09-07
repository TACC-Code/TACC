class BackupThread extends Thread {
    public Landmark(final String name, final Region region, final Hub hub, int mode, int phy, int antenna) throws IllegalArgumentException {
        if (hub == null) throw new IllegalArgumentException("Cannot create a landmark with a null hub.");
        this.hub = hub;
        this.mode = mode;
        this.phy = phy;
        this.antenna = antenna;
        if (region != null) this.regions.add(region);
        this.name = name;
        byte[] sha1Bytes = null;
        synchronized (Landmark.digest) {
            Landmark.digest.reset();
            Landmark.digest.update(this.hub.getID().toByteArray());
            Landmark.digest.update((byte) this.phy);
            Landmark.digest.update((byte) this.mode);
            Landmark.digest.update((byte) this.antenna);
            sha1Bytes = Landmark.digest.digest();
        }
        if (sha1Bytes == null) {
            System.err.println("Can't compute hash code: " + this.hub.getID() + "/" + this.hub.getName() + "/" + this.phy + "/" + this.antenna);
            System.exit(1);
        }
        int hashCode = 0;
        for (int i = 0; i < sha1Bytes.length; i++) {
            hashCode ^= (int) sha1Bytes[i] << ((i % 4) * 8);
        }
        this.hashCode = hashCode;
    }
}
