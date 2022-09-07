class BackupThread extends Thread {
    public void test_writeI() throws Exception {
        DigestOutputStream dos = new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance("SHA"));
        dos.write((byte) 43);
        byte digestResult[] = dos.getMessageDigest().digest();
        byte expected[] = { -87, 121, -17, 16, -52, 111, 106, 54, -33, 107, -118, 50, 51, 7, -18, 59, -78, -30, -37, -100 };
        assertTrue("Digest did not return expected result.", java.util.Arrays.equals(digestResult, expected));
    }
}
