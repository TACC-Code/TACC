class BackupThread extends Thread {
    public void test_onZ() throws Exception {
        DigestOutputStream dos = new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance("SHA"));
        dos.on(false);
        byte digestArray[] = { 23, 43, 44 };
        dos.write(digestArray, 1, 1);
        byte digestResult[] = dos.getMessageDigest().digest();
        byte expected[] = { -38, 57, -93, -18, 94, 107, 75, 13, 50, 85, -65, -17, -107, 96, 24, -112, -81, -40, 7, 9 };
        assertTrue("Digest did not return expected result.", java.util.Arrays.equals(digestResult, expected));
        dos.on(true);
        dos.write(digestArray, 1, 1);
        digestResult = dos.getMessageDigest().digest();
        byte expected1[] = { -87, 121, -17, 16, -52, 111, 106, 54, -33, 107, -118, 50, 51, 7, -18, 59, -78, -30, -37, -100 };
        assertTrue("Digest did not return expected result.", java.util.Arrays.equals(digestResult, expected1));
    }
}
