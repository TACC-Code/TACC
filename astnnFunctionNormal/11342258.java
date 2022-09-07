class BackupThread extends Thread {
    public static final long computeHash(byte[] bytes) throws XMLStreamException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(bytes);
        BigInteger number = new BigInteger(1, messageDigest);
        return number.longValue();
    }
}
