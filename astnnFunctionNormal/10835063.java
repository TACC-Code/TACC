class BackupThread extends Thread {
    public static byte[] getSHAHashBytes(byte[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        return digest.digest(input);
    }
}
