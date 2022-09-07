class BackupThread extends Thread {
    protected byte[] engineDigest() {
        byte[] digest = md5.digest();
        md5.update(opad);
        return md5.digest(digest);
    }
}
