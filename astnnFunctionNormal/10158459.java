class BackupThread extends Thread {
    public static byte[] my_cancel_secret_hash(byte[] secret) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.CLIENT_CANCEL_TAG));
        _hasher.update(secret);
        return _hasher.digest();
    }
}
