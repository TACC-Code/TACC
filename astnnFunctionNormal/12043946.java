class BackupThread extends Thread {
    public PublisherID(PublisherPublicKeyDigest keyID) {
        this(keyID.digest(), PublisherType.KEY);
    }
}
