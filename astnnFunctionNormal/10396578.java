class BackupThread extends Thread {
    public static byte[] digest(byte[] input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        return md.digest(input);
    }
}
