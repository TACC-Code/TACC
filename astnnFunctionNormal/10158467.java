class BackupThread extends Thread {
    public static byte[] ssk_storage_index_hash(byte[] key) {
        SHA256d _hasher = new SHA256d(Tags.keylen);
        _hasher.update(NetString.toNetString(Tags.MUTABLE_STORAGEINDEX_TAG));
        _hasher.update(key);
        return _hasher.digest();
    }
}
