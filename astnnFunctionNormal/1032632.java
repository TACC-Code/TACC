class BackupThread extends Thread {
    public static byte[] digest(byte[] input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(input);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
        }
        return null;
    }
}
