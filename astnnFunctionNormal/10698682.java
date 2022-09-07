class BackupThread extends Thread {
    public static String digestBytesToString(byte[] b_value, String method) throws Exception {
        MessageDigest algo = MessageDigest.getInstance(method);
        algo.reset();
        algo.update(b_value);
        byte[] buffer = algo.digest();
        return StringTool.byteArrayToString(buffer);
    }
}
