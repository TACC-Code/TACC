class BackupThread extends Thread {
    public void testGetUnwrappedKeySuperseded() throws Exception {
        ContentName supersededKeyDirectoryName = ContentName.fromNative(keyDirectoryBase + rand.nextInt(10000) + "/superseded");
        ContentName versionSupersededKeyDirectoryName = VersioningProfile.addVersion(supersededKeyDirectoryName);
        CCNHandle handle = CCNHandle.open();
        PrincipalKeyDirectory skd = new PrincipalKeyDirectory(acm, versionSupersededKeyDirectoryName, handle);
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        Key supersededAESSecretKey = kg.generateKey();
        byte[] expectedKeyID = CCNDigestHelper.digest(supersededAESSecretKey.getEncoded());
        ContentName supersedingKeyName = keyDirectoryName;
        skd.addSupersededByBlock(supersededAESSecretKey, supersedingKeyName, null, AESSecretKey);
        while (!skd.hasChildren() || !skd.hasSupersededBlock()) skd.waitForNewChildren();
        Assert.assertTrue(skd.hasSupersededBlock());
        Assert.assertNotNull(skd.getSupersededBlockName());
        Key unwrappedSecretKey = skd.getUnwrappedKey(expectedKeyID);
        Assert.assertEquals(supersededAESSecretKey, unwrappedSecretKey);
        skd.stopEnumerating();
    }
}
