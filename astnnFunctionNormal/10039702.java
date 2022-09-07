class BackupThread extends Thread {
    public static byte[] byteDigest(byte[] data, String messageDigestAlgorithm) throws CryptoException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(messageDigestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        md.reset();
        md.update(data);
        return md.digest();
    }
}
