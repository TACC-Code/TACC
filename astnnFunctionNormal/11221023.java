class BackupThread extends Thread {
    static byte[] calculateMIC(SharedSecret secret, byte[] agent) {
        if (secret == null) {
            return null;
        }
        if (agent == null) {
            return null;
        }
        _mdigest.reset();
        _mdigest.update(agent);
        _mdigest.update(secret.secret());
        return _mdigest.digest();
    }
}
