class BackupThread extends Thread {
    public byte[] digest(byte input[]) {
        return algorithm.digest(input);
    }
}
