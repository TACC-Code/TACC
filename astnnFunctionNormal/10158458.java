class BackupThread extends Thread {
    public static byte[] my_renewal_secret_hash(byte[] secret) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.CLIENT_RENEWAL_TAG));
        _hasher.update(secret);
        return _hasher.digest();
    }
}
