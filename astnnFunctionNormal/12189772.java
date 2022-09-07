class BackupThread extends Thread {
    public static String getHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(data);
        String hexHash = byte2hex(hash);
        return hexHash;
    }
}
