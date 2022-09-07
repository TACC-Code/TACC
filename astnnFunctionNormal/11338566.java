class BackupThread extends Thread {
    @Test
    public void testNullSecureDigest() {
        IDigester secureDigester = DigesterFactory.getInstance().getSecureDigester();
        String digest = secureDigester.digest(null);
        Assert.assertEquals(digest, null);
    }
}
