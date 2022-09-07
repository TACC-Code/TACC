class BackupThread extends Thread {
    public static final String hash(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] hash = MessageDigest.getInstance("SHA-1").digest(s.getBytes());
        return hex(hash);
    }
}
