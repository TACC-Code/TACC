class BackupThread extends Thread {
    private static String H(String data, MessageDigest digest) {
        digest.reset();
        byte[] x = digest.digest(data.getBytes());
        return cvtHex(x);
    }
}
