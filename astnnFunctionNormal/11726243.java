class BackupThread extends Thread {
    public static String hashSHA(String str) {
        byte[] b = str.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            md.update(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return toHexString(md.digest());
    }
}
