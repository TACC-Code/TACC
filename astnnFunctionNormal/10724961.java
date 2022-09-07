class BackupThread extends Thread {
    public LandmarkInfo(HubInfo hubInfo, int physicalLayer, int antenna) {
        if (hubInfo == null) throw new NullPointerException("Cannot create a LandmarkInfo object with a null HubInfo object.");
        this.hubInfo = hubInfo;
        this.physicalLayer = physicalLayer;
        this.antenna = antenna;
        byte[] sha1Bytes = null;
        synchronized (LandmarkInfo.digest) {
            LandmarkInfo.digest.reset();
            LandmarkInfo.digest.update(this.hubInfo.getAddress().toByteArray());
            LandmarkInfo.digest.update((byte) this.physicalLayer);
            sha1Bytes = LandmarkInfo.digest.digest(new byte[] { (byte) this.antenna });
        }
        if (sha1Bytes == null) {
            System.err.println("Can't compute hash code: " + this.hubInfo.getAddress() + "/" + this.hubInfo.name + "/" + this.physicalLayer + "/" + this.antenna);
            System.exit(1);
        }
        int hashCode = 0;
        for (int i = 0; i < sha1Bytes.length; i++) {
            hashCode += (int) sha1Bytes[i] << (i % 4);
        }
        this.hashCode = hashCode;
        URL connected = this.getClass().getResource("/non-java/images/rxer-on.png");
        URL disconnected = this.getClass().getResource("/non-java/images/rxer-off.png");
        try {
            this.iconConnected = ImageIO.read(connected);
            this.iconDisconnected = ImageIO.read(disconnected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
