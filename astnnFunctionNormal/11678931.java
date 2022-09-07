class BackupThread extends Thread {
    public byte[] generateHash(byte data[]) throws InvalidParameterException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(internalAlgorithmName);
        } catch (NoSuchAlgorithmException x) {
            throw new RuntimeException(internalAlgorithmName + " hash algorithm not found");
        }
        return digest.digest(data);
    }
}
