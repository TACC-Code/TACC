class BackupThread extends Thread {
    public static byte[] ssk_writekey_hash(byte[] key) {
        SHA256d _hasher = new SHA256d(Tags.keylen);
        _hasher.update(NetString.toNetString(Tags.MUTABLE_WRITEKEY_TAG));
        _hasher.update(key);
        return _hasher.digest();
    }
}
