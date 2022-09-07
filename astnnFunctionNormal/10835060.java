class BackupThread extends Thread {
    public static String getSHAHash(byte[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        return StringUtils.getHexString(digest.digest(input));
    }
}
