class BackupThread extends Thread {
    public static byte[] bucket_cancel_secret(byte[] file_cancel_secret, byte[] peerid) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.BUCKET_CANCEL_TAG));
        _hasher.update(NetString.toNetString(file_cancel_secret));
        _hasher.update(NetString.toNetString(peerid));
        return _hasher.digest();
    }
}
