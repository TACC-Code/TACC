class BackupThread extends Thread {
    public byte[] sign(final InputStream in) throws CryptException, IOException {
        final BigInteger[] out = signer.generateSignature(digest.digest(in));
        return encode(out[0], out[1]);
    }
}
