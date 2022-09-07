class BackupThread extends Thread {
    public void getDigest(byte[] out, int off) {
        md.digest(out, off);
    }
}
