class BackupThread extends Thread {
    private static String hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
        String hashString = new String(Base64.encodeBase64(hashBytes), "UTF-8");
        return hashString;
    }
}
