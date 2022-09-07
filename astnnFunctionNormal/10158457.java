class BackupThread extends Thread {
    public static byte[] pair_hash(byte[] a, byte[] b) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.PAIR_HASH_TAG));
        _hasher.update(NetString.toNetString(a));
        _hasher.update(NetString.toNetString(b));
        return _hasher.digest();
    }
}
