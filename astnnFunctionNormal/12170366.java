class BackupThread extends Thread {
    public byte[] digest() {
        return algorithm.digest();
    }
}
