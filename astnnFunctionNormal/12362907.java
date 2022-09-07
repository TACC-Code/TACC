class BackupThread extends Thread {
    private String md5(byte[] data) {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            hash.update(data);
            return String.format("%032x", new BigInteger(1, hash.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
