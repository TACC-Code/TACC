class BackupThread extends Thread {
    public static String hashMD5(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return toHexString(md.digest());
    }
}
