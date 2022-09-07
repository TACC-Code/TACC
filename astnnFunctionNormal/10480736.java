class BackupThread extends Thread {
    public static String digest(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] data = digest.digest(password.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return new String(encoder.encode(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
