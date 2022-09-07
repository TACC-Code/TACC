class BackupThread extends Thread {
    public static byte[] md5(String plainText) {
        byte[] defaultBytes = plainText.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
}
