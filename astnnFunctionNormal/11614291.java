class BackupThread extends Thread {
    public String digest(String schema, String nonce, String password) throws ImException {
        return mPasswordDigest.digest(schema, nonce, password);
    }
}
