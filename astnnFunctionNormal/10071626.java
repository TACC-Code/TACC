class BackupThread extends Thread {
    protected byte[] engineSign() throws SignatureException {
        int digestLength = md_.getDigestLength();
        byte[] salt = new byte[digestLength];
        secureRandom_.nextBytes(salt);
        byte[] message = baos_.toByteArray();
        try {
            baos_.close();
        } catch (IOException ioe) {
            System.out.println("shouldn't happen");
            ioe.printStackTrace();
        }
        md_.update(salt);
        byte[] h = md_.digest(message);
        byte[] ps = new byte[cipherBlockSize_ - 1 - 2 * digestLength];
        byte[] db = new byte[cipherBlockSize_ - 1 - digestLength];
        System.arraycopy(salt, 0, db, 0, digestLength);
        System.arraycopy(ps, 0, db, digestLength, cipherBlockSize_ - 1 - 2 * digestLength);
        byte[] dbMask = mgf(h, cipherBlockSize_ - 1 - digestLength);
        byte[] maskedDB = xor(db, dbMask);
        byte[] em = new byte[cipherBlockSize_ - 1];
        System.arraycopy(h, 0, em, 0, digestLength);
        System.arraycopy(maskedDB, 0, em, digestLength, cipherBlockSize_ - 1 - digestLength);
        BigInteger m = new BigInteger(1, em);
        BigInteger c;
        if (rsaPrivateKey_ instanceof RSAPrivateKey) {
            BigInteger n = rsaPrivateKey_.getModulus();
            BigInteger d = rsaPrivateKey_.getPrivateExponent();
            c = m.modPow(d, n);
        } else {
            RSAPrivateCrtKey rsaPrivCrtKey = (RSAPrivateCrtKey) rsaPrivateKey_;
            BigInteger d = rsaPrivCrtKey.getPrivateExponent();
            BigInteger p = rsaPrivCrtKey.getPrimeP();
            BigInteger q = rsaPrivCrtKey.getPrimeQ();
            BigInteger dP = rsaPrivCrtKey.getPrimeExponentP();
            BigInteger dQ = rsaPrivCrtKey.getPrimeExponentQ();
            BigInteger qInv = rsaPrivCrtKey.getCrtCoefficient();
            BigInteger m_1, m_2, hBI;
            m_1 = (m.remainder(p)).modPow(dP, p);
            m_2 = (m.remainder(q)).modPow(dQ, q);
            hBI = (qInv.multiply((m_1.subtract(m_2)).remainder(p))).mod(p);
            c = ((hBI.multiply(q)).add(m_2));
        }
        return getBytes(c);
    }
}
