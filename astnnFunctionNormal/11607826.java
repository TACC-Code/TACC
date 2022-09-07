class BackupThread extends Thread {
    private static String md5Hex(byte[] bytes) {
        return new String(encodeHex(getMd5Digest().digest(bytes)));
    }
}
