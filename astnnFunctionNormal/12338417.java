class BackupThread extends Thread {
    public void testGetUnwrappedKeyGroupMember() throws Exception {
        ContentName myIdentity = ContentName.fromNative(userStore, "pgolle");
        acm.publishMyIdentity(myIdentity, null);
        String randomGroupName = "testGroup" + rand.nextInt(10000);
        ArrayList<Link> newMembers = new ArrayList<Link>();
        newMembers.add(new Link(myIdentity));
        Group myGroup = acm.groupManager().createGroup(randomGroupName, newMembers, 0);
        Assert.assertTrue(acm.groupManager().haveKnownGroupMemberships());
        Thread.sleep(5000);
        PrincipalKeyDirectory pkd = myGroup.privateKeyDirectory(acm);
        pkd.waitForChildren();
        Assert.assertTrue(pkd.hasPrivateKeyBlock());
        ContentName versionDirectoryName2 = VersioningProfile.addVersion(ContentName.fromNative(keyDirectoryBase + Integer.toString(rand.nextInt(10000))));
        PrincipalKeyDirectory kd2 = new PrincipalKeyDirectory(acm, versionDirectoryName2, handle);
        PublicKey groupPublicKey = myGroup.publicKey();
        ContentName groupPublicKeyName = myGroup.publicKeyName();
        kd2.addWrappedKeyBlock(AESSecretKey, groupPublicKeyName, groupPublicKey);
        byte[] expectedKeyID = CCNDigestHelper.digest(AESSecretKey.getEncoded());
        kd2.waitForChildren();
        Thread.sleep(10000);
        Key unwrappedSecretKey = kd2.getUnwrappedKey(expectedKeyID);
        Assert.assertEquals(AESSecretKey, unwrappedSecretKey);
        kd2.stopEnumerating();
    }
}
