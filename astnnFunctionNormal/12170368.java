class BackupThread extends Thread {
    public int digest(byte buf[], int offset, int len) throws java.security.DigestException {
        return algorithm.digest(buf, offset, len);
    }
}
