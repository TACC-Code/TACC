class BackupThread extends Thread {
    public static byte[] file_renewal_secret(byte[] client_renewal_secret, byte[] storage_index) {
        SHA256d _hasher = new SHA256d();
        _hasher.update(NetString.toNetString(Tags.FILE_RENEWAL_TAG));
        _hasher.update(NetString.toNetString(client_renewal_secret));
        _hasher.update(NetString.toNetString(storage_index));
        return _hasher.digest();
    }
}
