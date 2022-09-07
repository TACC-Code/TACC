class BackupThread extends Thread {
    public static String encrypt(String str) {
        try {
            byte[] chars = str.getBytes();
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(chars, 0, chars.length);
            MD5.data = new BigInteger(1, m.digest());
        } catch (NoSuchAlgorithmException alg) {
            alg.printStackTrace();
        }
        return String.format("%1$032X", MD5.data);
    }
}
