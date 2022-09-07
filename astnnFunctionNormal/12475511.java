class BackupThread extends Thread {
    public byte[] sign(final byte[] data) throws CryptException {
        final BigInteger[] out = signer.generateSignature(digest.digest(data));
        return encode(out[0], out[1]);
    }
}
