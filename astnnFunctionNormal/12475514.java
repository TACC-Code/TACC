class BackupThread extends Thread {
    public boolean verify(final InputStream in, final byte[] signature) throws CryptException, IOException {
        final BigInteger[] sig = decode(signature);
        return signer.verifySignature(digest.digest(in), sig[0], sig[1]);
    }
}
