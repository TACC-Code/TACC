class BackupThread extends Thread {
    public final byte[] getPublisher() {
        return _publisher.digest();
    }
}
