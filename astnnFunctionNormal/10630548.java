class BackupThread extends Thread {
    public static String shaHash(Object value) {
        return new BASE64Encoder().encode(MD.digest(value.toString().getBytes()));
    }
}
