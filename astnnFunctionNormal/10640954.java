class BackupThread extends Thread {
    public static String getMd5(byte[] input) throws NoSuchAlgorithmException {
        if (algorithm == null) {
            algorithm = MessageDigest.getInstance("MD5");
        }
        algorithm.reset();
        algorithm.update(input);
        byte messageDigest[] = algorithm.digest();
        BigInteger number = new BigInteger(1, messageDigest);
        String md5val = number.toString(16);
        int prefixZeros = 32 - md5val.length();
        for (int i = 0; i < prefixZeros; ++i) {
            md5val = "0" + md5val;
        }
        return md5val.toUpperCase();
    }
}
