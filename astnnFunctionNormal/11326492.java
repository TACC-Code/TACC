class BackupThread extends Thread {
    private static byte[] encrypt(String password) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(password.getBytes("ISO8859-1"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
