class BackupThread extends Thread {
    public static byte[] computeSHA1HMac(String data, String cookie) throws GeneralSecurityException {
        byte[] hash;
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        hash = digest.digest(cookie.getBytes());
        byte[] hmacResult;
        Mac hm;
        hm = Mac.getInstance("HMacSHA1");
        Key k1 = new SecretKeySpec(hash, 0, hash.length, "HMacSHA1");
        hm.init(k1);
        return hm.doFinal(data.getBytes());
    }
}
