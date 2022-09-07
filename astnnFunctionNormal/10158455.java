class BackupThread extends Thread {
    public static byte[] block_hash(byte[] data) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.BLOCK_TAG));
        _hasher.update(data);
        return _hasher.digest();
    }
}
