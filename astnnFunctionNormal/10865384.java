class BackupThread extends Thread {
    public static String encodePassword(String clearPassword) {
        final byte[] s;
        synchronized (getMessageDigest()) {
            getMessageDigest().reset();
            getMessageDigest().update(clearPassword.getBytes());
            s = getMessageDigest().digest();
        }
        return Base64.encodeBytes(s);
    }
}
