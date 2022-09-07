class BackupThread extends Thread {
    public static String hash(char[] password, boolean uppercase) {
        instance.digest.reset();
        if (null != password) {
            instance.digest.update(createBuffer(password));
            Arrays.fill(password, '\0');
        }
        byte[] result = instance.digest.digest();
        return translateToHex(result, uppercase);
    }
}
