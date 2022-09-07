class BackupThread extends Thread {
    public static byte[] getMD5ByteHash(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(input);
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
