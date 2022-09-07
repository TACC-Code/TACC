class BackupThread extends Thread {
    private SRPDigest(MessageDigest md) {
        super();
        this.md = md;
        digestLength = md.digest().length;
    }
}
