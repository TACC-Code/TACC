class BackupThread extends Thread {
    public static String MD5Encoder(String msg) {
        init();
        return StringUtils.byteArrayToHexString(md5.digest(msg.getBytes()));
    }
}
