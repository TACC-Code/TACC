class BackupThread extends Thread {
    public static String calculateMD5(byte[] aData) {
        String zReturn = "";
        try {
            java.security.MessageDigest zMD = java.security.MessageDigest.getInstance("MD5");
            zMD.update(aData);
            byte[] zSum = zMD.digest();
            java.math.BigInteger zBig = new java.math.BigInteger(1, zSum);
            zReturn = zBig.toString(16);
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return zReturn;
    }
}
