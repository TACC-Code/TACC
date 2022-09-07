class BackupThread extends Thread {
    public static String criptString(String pswd) {
        StringBuilder xyz = new StringBuilder();
        try {
            byte inarray[] = pswd.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(inarray, 0, inarray.length);
            byte outarray[] = md5.digest();
            for (int i = 0; i < outarray.length; i++) {
                xyz.append(outarray[i]);
            }
        } catch (NoSuchAlgorithmException ex) {
        }
        return xyz.toString();
    }
}
