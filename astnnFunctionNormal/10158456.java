class BackupThread extends Thread {
    public static byte[] empty_leaf_hash(int val) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.EMPTY_LEAF_HASH));
        _hasher.update(String.format("%d", val));
        return _hasher.digest();
    }
}
