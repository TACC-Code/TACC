class BackupThread extends Thread {
    public static byte[] md5(byte[] bin) {
        MessageDigest md5 = getMD5();
        return md5.digest(bin);
    }
}
