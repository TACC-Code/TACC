class BackupThread extends Thread {
    public static byte[] sha1(byte[] bin) {
        MessageDigest sha1 = getSHA1();
        return sha1.digest(bin);
    }
}
