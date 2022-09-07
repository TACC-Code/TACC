class BackupThread extends Thread {
    public void test_write$BII() throws Exception {
        DigestOutputStream dos = new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance("SHA"));
        byte digestArray[] = { 23, 43, 44 };
        dos.write(digestArray, 1, 1);
        byte digestResult[] = dos.getMessageDigest().digest();
        byte expected[] = { -87, 121, -17, 16, -52, 111, 106, 54, -33, 107, -118, 50, 51, 7, -18, 59, -78, -30, -37, -100 };
        assertTrue("Digest did not return expected result.", java.util.Arrays.equals(digestResult, expected));
    }
}
