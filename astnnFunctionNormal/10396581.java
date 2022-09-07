class BackupThread extends Thread {
    public static String digest(String value) {
        byte[] b;
        try {
            b = CriptoUtils.digest(value.getBytes(), encryptlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return CriptoUtils.byteArrayToHexString(b);
    }
}
