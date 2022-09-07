class BackupThread extends Thread {
    public static byte[] doubleSha256(byte[] data, int offset, int length) {
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance(SHA256);
            digest.update(data, offset, length);
            return digest.digest(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
