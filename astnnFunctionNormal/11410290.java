class BackupThread extends Thread {
    public static byte[] hashString(String arg) {
        MessageDigest md = getMessageDigest();
        md.update(arg.getBytes());
        return md.digest();
    }
}
