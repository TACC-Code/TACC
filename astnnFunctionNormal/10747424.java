class BackupThread extends Thread {
    public static byte[] getDigest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(SALT);
            return digest.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
