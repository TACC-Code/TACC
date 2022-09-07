class BackupThread extends Thread {
    public static byte[] ssk_pubkey_fingerprint_hash(byte[] pubkey) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.MUTABLE_PUBKEY_TAG));
        _hasher.update(pubkey);
        return _hasher.digest();
    }
}
