class BackupThread extends Thread {
    public byte[] digest(byte[] src) {
        return newDigest().digest(src);
    }
}
