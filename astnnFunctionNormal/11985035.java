class BackupThread extends Thread {
    public static String calculate(String str, String algorithm) {
        try {
            byte[] b = ComputeDigest.digest(str.getBytes(), algorithm);
            return ComputeDigest.byteArrayToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
