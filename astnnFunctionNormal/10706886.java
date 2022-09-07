class BackupThread extends Thread {
    protected int engineDigest(byte[] buf, int offset, int len) {
        byte[] digest = md5.digest();
        md5.update(opad);
        md5.update(digest);
        try {
            return md5.digest(buf, offset, len);
        } catch (Exception ex) {
            throw new IllegalStateException();
        }
    }
}
