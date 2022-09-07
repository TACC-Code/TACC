class BackupThread extends Thread {
    public static byte[] getSHABytes(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException nsae) {
        }
        return null;
    }
}
