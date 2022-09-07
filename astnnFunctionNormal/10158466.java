class BackupThread extends Thread {
    public static byte[] ssk_readkey_hash(byte[] writekey) {
        SHA256d _hasher = new SHA256d(Tags.keylen);
        _hasher.update(NetString.toNetString(Tags.MUTABLE_READKEY_TAG));
        _hasher.update(writekey);
        return _hasher.digest();
    }
}
