class BackupThread extends Thread {
    public BigInteger generateCoinNumber(final PublicBank bank) throws NoSuchAlgorithmException {
        int nCoinLength = (m_biCoinID.bitLength() + 7) / 8;
        int nDigestIterations = (bank.getPrimeLength() - nCoinLength) / PublicBank.DIGEST_LENGTH;
        int n;
        if (nCoinLength > bank.getCoinLength()) {
            return null;
        }
        byte[] xplusd = new byte[bank.getPrimeLength()];
        for (n = 0; n < (bank.getCoinLength() - nCoinLength); ++n) xplusd[n] = 0;
        Util.byteCopy(xplusd, n, m_biCoinID.toByteArray(), 0, nCoinLength);
        nCoinLength += n;
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        for (n = 0; n < nDigestIterations; ++n) {
            sha1.update(xplusd, 0, nCoinLength + (PublicBank.DIGEST_LENGTH * n));
            Util.byteCopy(xplusd, nCoinLength + (PublicBank.DIGEST_LENGTH * n), sha1.digest(), 0, PublicBank.DIGEST_LENGTH);
        }
        BigInteger bi = new BigInteger(xplusd);
        return bi;
    }
}
