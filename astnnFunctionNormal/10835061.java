class BackupThread extends Thread {
    public static String getSHAHash(byte[] input, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        digest.update(input);
        return StringUtils.getHexString(digest.digest());
    }
}
