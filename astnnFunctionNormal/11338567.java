class BackupThread extends Thread {
    @Test
    public void testNullSaltedSecureDigest() {
        IDigester secureDigester = DigesterFactory.getInstance().getSaltedSecureDigester();
        String digest = secureDigester.digest(null);
        Assert.assertEquals(digest, null);
    }
}
