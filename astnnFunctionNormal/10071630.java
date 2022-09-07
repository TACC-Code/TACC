class BackupThread extends Thread {
    protected boolean engineVerify(byte[] signature) {
        int digestLength = md_.getDigestLength();
        BigInteger c = new BigInteger(1, signature);
        BigInteger n = rsaPublicKey_.getModulus();
        BigInteger e = rsaPublicKey_.getPublicExponent();
        BigInteger message;
        if (c.compareTo(n) >= 0) {
            return false;
        }
        if (e.equals(THREE)) {
            message = (((c.multiply(c)).mod(n)).multiply(c)).mod(n);
        } else {
            message = c.modPow(e, n);
        }
        byte[] em = getBytes(message);
        byte[] h = new byte[digestLength];
        System.arraycopy(em, 0, h, 0, digestLength);
        byte[] maskedDB = new byte[em.length - digestLength];
        System.arraycopy(em, digestLength, maskedDB, 0, em.length - digestLength);
        byte[] dbMask = mgf(h, em.length - digestLength);
        byte[] db = xor(maskedDB, dbMask);
        byte[] salt = new byte[digestLength];
        System.arraycopy(db, 0, salt, 0, digestLength);
        byte[] ps = new byte[em.length - 2 * digestLength];
        System.arraycopy(db, digestLength, ps, 0, em.length - 2 * digestLength);
        for (int i = 0; i < (em.length - 2 * digestLength); i++) {
            if (ps[i] != 0x00) {
                return false;
            }
        }
        byte[] msg = baos_.toByteArray();
        try {
            baos_.close();
        } catch (IOException ioe) {
            System.out.println("shouldn't happen");
            ioe.printStackTrace();
        }
        md_.update(salt);
        byte[] hs = md_.digest(msg);
        for (int i = 0; i < digestLength; i++) {
            if (hs[i] != h[i]) {
                return false;
            }
        }
        return true;
    }
}
