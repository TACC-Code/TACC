class BackupThread extends Thread {
    public byte[] digest() {
        return md5.digest();
    }
}
