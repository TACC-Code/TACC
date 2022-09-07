class BackupThread extends Thread {
    public static final byte[] generateHash(final String clearTextID) {
        String id;
        id = clearTextID;
        byte[] buffer = id.getBytes();
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            logger.fatal("Cannot load selected Digest Hash implementation", e);
            return null;
        }
        algorithm.reset();
        algorithm.update(buffer);
        try {
            byte[] digest1 = algorithm.digest();
            return digest1;
        } catch (Exception de) {
            logger.fatal("Failed to creat a digest.", de);
            return null;
        }
    }
}
