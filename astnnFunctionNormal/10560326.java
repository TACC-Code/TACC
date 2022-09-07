class BackupThread extends Thread {
    public static String getFingerPrint(byte[] xml, String algo) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algo);
        byte[] hash = digest.digest(xml);
        return toHexa(hash);
    }
}
