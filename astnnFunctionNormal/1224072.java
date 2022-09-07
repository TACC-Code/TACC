class BackupThread extends Thread {
    public static String md5sum(String s) {
        StringBuffer hash = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] b = s.getBytes();
            b = md5.digest(b);
            for (byte bb : b) {
                String hex = Integer.toHexString((int) bb & 0xff);
                while (hex.length() < 2) hex = '0' + hex;
                hash.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return hash.toString();
    }
}
