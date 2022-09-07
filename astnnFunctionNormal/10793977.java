class BackupThread extends Thread {
    public static final String toHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] binary = digest.digest(password.getBytes());
            return toHex(binary);
        } catch (Throwable t) {
            logger.warn(String.format("Could not convert password '%s' to a SHA1 hash: %s", password, t.getMessage()));
        }
        return null;
    }
}
