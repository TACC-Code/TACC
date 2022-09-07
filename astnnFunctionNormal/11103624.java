class BackupThread extends Thread {
    public static byte[] sha(byte[] content) {
        if (null == content) return null;
        byte[] result = null;
        try {
            result = MessageDigest.getInstance("SHA").digest(content);
        } catch (NoSuchAlgorithmException ex) {
            logger.warn("Cannot find SHA algorithm!", ex);
        }
        return result;
    }
}
