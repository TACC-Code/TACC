class BackupThread extends Thread {
    public static byte[] file_cancel_secret(byte[] client_cancel_secret, byte[] storage_index) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.FILE_CANCEL_TAG));
        _hasher.update(NetString.toNetString(client_cancel_secret));
        _hasher.update(NetString.toNetString(storage_index));
        return _hasher.digest();
    }
}
