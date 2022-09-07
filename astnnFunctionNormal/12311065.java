class BackupThread extends Thread {
    public static String getSAH256HashBase64(byte[] toHash) throws NoSuchAlgorithmException {
        final MessageDigest sha256 = MessageDigest.getInstance("SHA256", new BouncyCastleProvider());
        byte[] result = sha256.digest(toHash);
        return new String(Base64.encode(result));
    }
}
