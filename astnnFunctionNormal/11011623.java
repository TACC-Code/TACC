class BackupThread extends Thread {
    public static String getMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        byte[] bytes = s.getBytes();
        digester.update(bytes);
        return digester.digest().toString();
    }
}
