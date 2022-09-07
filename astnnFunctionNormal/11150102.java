class BackupThread extends Thread {
    public static byte[] getMD5Bytes(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (java.io.UnsupportedEncodingException uee) {
            throw new IllegalStateException(uee);
        }
    }
}
