class BackupThread extends Thread {
    @Test
    public void testSecureDigest() {
        IDigester simpleDigester = DigesterFactory.getInstance().getSecureDigester();
        String secureString = simpleDigester.digest("kapil");
        Assert.assertEquals(secureString, "{SHA}/KtCPCInoz9L+EoRDHfHxvvubXk=");
    }
}
