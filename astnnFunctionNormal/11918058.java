class BackupThread extends Thread {
    public static byte[] hash(byte[] key) throws Exception {
        SHA1 sha = new SHA1();
        sha.init();
        sha.update(key, 0, key.length);
        sha.finish();
        return sha.digest();
    }
}
