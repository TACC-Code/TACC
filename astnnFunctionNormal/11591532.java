class BackupThread extends Thread {
    private static String generateCash(String prefix, int value, MessageDigest md) throws NoSuchAlgorithmException {
        Random rnd = new Random();
        long random = rnd.nextLong();
        long counter = rnd.nextLong();
        prefix = prefix + Long.toHexString(random) + ":";
        String temp;
        int tempValue;
        byte[] bArray;
        do {
            counter++;
            temp = prefix + Long.toHexString(counter);
            md.reset();
            md.update(temp.getBytes());
            bArray = md.digest();
            tempValue = numberOfLeadingZeros(bArray);
        } while (tempValue < value);
        return temp;
    }
}
