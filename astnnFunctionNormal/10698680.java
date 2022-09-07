class BackupThread extends Thread {
    public static String digest(String value, String method) throws Exception {
        byte[] b_value = value.getBytes("UTF-8");
        MessageDigest algo = MessageDigest.getInstance(method);
        algo.reset();
        algo.update(b_value);
        byte[] buffer = algo.digest();
        return StringTool.byteArrayToString(buffer);
    }
}
