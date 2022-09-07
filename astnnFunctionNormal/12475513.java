class BackupThread extends Thread {
    public boolean verify(final byte[] data, final byte[] signature) throws CryptException {
        final BigInteger[] sig = decode(signature);
        return signer.verifySignature(digest.digest(data), sig[0], sig[1]);
    }
}
