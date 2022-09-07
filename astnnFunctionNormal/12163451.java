class BackupThread extends Thread {
    public static byte[] computeMD5Hash(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
            return md5.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptographyHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
