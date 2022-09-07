class BackupThread extends Thread {
    public static String getHash(final String algorithm, final String input) throws SQLException {
        String output = "";
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final byte mdBytes[] = input.getBytes();
            md.update(mdBytes);
            final byte mdsum[] = md.digest();
            final BigInteger bigInt = new BigInteger(1, mdsum);
            output = bigInt.toString(16);
            while (output.length() < (md.getDigestLength() * 2)) {
                output = "0" + output;
            }
            md.reset();
        } catch (final NoSuchAlgorithmException e) {
            throw new SQLException("NoSuchAlgorithmException: Error accessing a Java algorithm for: " + algorithm, "WCNAE", e);
        }
        return output;
    }
}
