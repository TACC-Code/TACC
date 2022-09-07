class BackupThread extends Thread {
    public static byte[] bucket_renewal_secret(byte[] file_renewal_secret, byte[] peerid) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.BUCKET_RENEWAL_TAG));
        _hasher.update(NetString.toNetString(file_renewal_secret));
        _hasher.update(NetString.toNetString(peerid));
        return _hasher.digest();
    }
}
