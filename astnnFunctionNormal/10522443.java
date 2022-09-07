class BackupThread extends Thread {
    public static String MD5(String src) {
        byte[] source = src.getBytes();
        try {
            MessageDigest alg = java.security.MessageDigest.getInstance("MD5 ");
            source = alg.digest(source);
        } catch (NoSuchAlgorithmException ex) {
        }
        return source.toString();
    }
}
