class BackupThread extends Thread {
    public static byte[] md5(byte[] content) {
        if (null == content) return null;
        byte[] result = null;
        try {
            result = MessageDigest.getInstance("MD5").digest(content);
        } catch (NoSuchAlgorithmException ex) {
            logger.warn("Cannot find MD5 algorithm!", ex);
        }
        return result;
    }
}
