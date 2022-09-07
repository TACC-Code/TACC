class BackupThread extends Thread {
    public synchronized byte[] digestAsByteArray(byte[] input) throws Exception {
        digestAgent.reset();
        byte[] digest = digestAgent.digest(input);
        return digest;
    }
}
