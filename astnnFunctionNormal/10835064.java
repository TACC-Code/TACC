class BackupThread extends Thread {
    public static byte[] getTigerHashBytes(byte[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        algorithm.reset();
        return algorithm.digest(input);
    }
}
