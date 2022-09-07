class BackupThread extends Thread {
    public static byte[] digest(byte[] b_value, String method) throws Exception {
        MessageDigest algo = MessageDigest.getInstance(method);
        algo.reset();
        algo.update(b_value);
        byte[] buffer = algo.digest();
        return buffer;
    }
}
