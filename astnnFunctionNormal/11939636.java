class BackupThread extends Thread {
    protected boolean engineVerify(byte[] sigBytes) {
        BigInteger sig = new BigInteger(1, sigBytes);
        byte[] m = sig.modPow(pubKey.getPublicExponent(), pubKey.getModulus()).toByteArray();
        byte[] hash = md.digest();
        int i = 1;
        if (m[0] != 1) return false;
        try {
            while (m[i] == (byte) 0xFF) i++;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        if (m[i] != 0) return false;
        i++;
        if (m[i] != 0x30) return false;
        i++;
        if ((i + m[i]) != (m.length - 1)) return false;
        i++;
        if (m[i] != 0x30) return false;
        i++;
        i += m[i];
        i++;
        if (m[i] != 4) return false;
        i++;
        if (m[i] != hash.length) return false;
        if ((i + m[i]) != m.length - 1) return false;
        i++;
        int j = hash.length;
        while (j-- > 0) if (m[i + j] != hash[j]) return false;
        return true;
    }
}
