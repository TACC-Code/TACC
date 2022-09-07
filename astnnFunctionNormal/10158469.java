class BackupThread extends Thread {
    public static byte[] ssk_readkey_data_hash(byte[] IV, byte[] readkey) {
        SHA256d _hasher = new SHA256d(Tags.keylen);
        _hasher.update(NetString.toNetString(Tags.MUTABLE_DATAKEY_TAG));
        _hasher.update(NetString.toNetString(IV));
        _hasher.update(NetString.toNetString(readkey));
        return _hasher.digest();
    }
}
