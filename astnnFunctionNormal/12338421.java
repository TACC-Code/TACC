class BackupThread extends Thread {
    public void testGetUnwrappedKey() throws Exception {
        byte[] expectedKeyID = CCNDigestHelper.digest(AESSecretKey.getEncoded());
        Key unwrappedSecretKey = kd.getUnwrappedKey(expectedKeyID);
        Assert.assertEquals(AESSecretKey, unwrappedSecretKey);
    }
}
