class BackupThread extends Thread {
    public static final byte[] generateHash(String clearTextID, String function) {
        String id;
        if (function == null) {
            id = clearTextID;
        } else {
            id = clearTextID + functionSeperator + function;
        }
        byte[] buffer = id.getBytes();
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            LOG.error("Cannot load selected Digest Hash implementation", e);
            return null;
        }
        algorithm.reset();
        algorithm.update(buffer);
        try {
            byte[] digest1 = algorithm.digest();
            return digest1;
        } catch (Exception de) {
            LOG.error("Failed to creat a digest.", de);
            return null;
        }
    }
}
