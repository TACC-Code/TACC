class BackupThread extends Thread {
    public void addWrappingKeyToACM() throws Exception {
        PrivateKey privKey = wrappingKeyPair.getPrivate();
        byte[] publicKeyIdentifier = CCNDigestHelper.digest(wrappingKeyPair.getPublic().getEncoded());
        handle.keyManager().getSecureKeyCache().addMyPrivateKey(publicKeyIdentifier, privKey);
    }
}
