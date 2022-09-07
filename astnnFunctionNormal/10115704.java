class BackupThread extends Thread {
    public static byte[] sha256(byte[] data, int offset, int length) {
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance(SHA256);
            digest.update(data, offset, length);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
