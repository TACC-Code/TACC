class BackupThread extends Thread {
    public static boolean isMD5DigestValid(String username, String realm, String md5Password) {
        byte b[];
        try {
            b = MessageDigest.getInstance("MD5").digest((username + ":" + realm + ":" + md5Password).getBytes());
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        BigInteger bi = new BigInteger(b);
        String s = bi.toString(16);
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        return s.equals(md5Password);
    }
}
