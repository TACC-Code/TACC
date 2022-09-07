class BackupThread extends Thread {
    public static byte[] getSHA256(byte[] toHash) {
        byte[] result;
        try {
            final MessageDigest sha256 = MessageDigest.getInstance("SHA256", new BouncyCastleProvider());
            result = sha256.digest(toHash);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
