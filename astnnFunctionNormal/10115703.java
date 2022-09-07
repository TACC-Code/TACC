class BackupThread extends Thread {
    public static byte[] sha256(byte[] data1, byte[] data2) {
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance(SHA256);
            digest.update(data1, 0, data1.length);
            digest.update(data2, 0, data2.length);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
