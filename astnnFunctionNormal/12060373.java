class BackupThread extends Thread {
    protected String encrypt(String password) {
        synchronized (md) {
            md.reset();
            md.update(password.getBytes());
            return encryption + ":" + Tools.convert(md.digest());
        }
    }
}
