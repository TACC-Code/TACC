class BackupThread extends Thread {
    private boolean test(byte[] argument, byte[] etalon) {
        MD4 messageDigest = new MD4();
        messageDigest.engineReset();
        byte[] hash = messageDigest.digest(argument);
        return Arrays.equals(hash, etalon);
    }
}
