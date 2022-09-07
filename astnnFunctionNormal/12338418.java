class BackupThread extends Thread {
    public void testAddWrappedKey() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        wrappingKeyPair = kpg.generateKeyPair();
        PublicKey publicKey = wrappingKeyPair.getPublic();
        wrappingPKID = CCNDigestHelper.digest(publicKey.getEncoded());
        publicKeyName = ContentName.fromNative(userStore, principalName);
        ContentName versionPublicKeyName = VersioningProfile.addVersion(publicKeyName);
        kd.addWrappedKeyBlock(AESSecretKey, versionPublicKeyName, publicKey);
    }
}
