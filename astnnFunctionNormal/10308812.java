class BackupThread extends Thread {
    public byte[] checksum(byte[] key) throws IOException {
        while (!eof) {
            read();
        }
        while (count != 0) {
            processByte(0);
        }
        return digest.digest(key);
    }
}
