class BackupThread extends Thread {
    private boolean gotChallenge() throws IOException {
        System.err.println("gotChallenge()");
        if (setError(readByte())) return false;
        if (setError(readByte())) return false;
        BigInteger B = getBigInteger(readByte(32));
        byte[] gb = readByte(readByte());
        BigInteger g = getBigInteger(gb);
        byte[] Nb = readByte(readByte());
        BigInteger N = getBigInteger(Nb);
        byte[] salt = readByte(32);
        BigInteger sb = getBigInteger(salt);
        byte unk3[] = readByte(16);
        int unk12 = readByte();
        if (sb.signum() == 0) {
            errorStr = "Account unknown by server";
            return false;
        }
        System.err.println("Got challenge, B=" + B.toString(16) + " g=" + g.intValue() + " N=" + N.toString(16) + " salt=" + sb.toString(16));
        Sha160 h = new Sha160();
        h.update(salt);
        h.update(authHash);
        BigInteger x = getBigInteger(h.digest());
        System.err.println("Computed x=" + x.toString(16));
        BigInteger v = g.modPow(x, N);
        System.err.println("Computed v=" + v.toString(16));
        BigInteger a = BigInteger.ONE.add(new BigInteger(128, new Random()));
        BigInteger A = g.modPow(a, N);
        System.err.println("Computed a=" + a.toString(16) + " A=" + A.toString(16));
        h.reset();
        h.update(getBytesOf(A));
        h.update(getBytesOf(B));
        BigInteger u = getBigInteger(h.digest());
        System.err.println("Computed u=" + u.toString(16));
        BigInteger k = new BigInteger("3");
        BigInteger S = B.subtract(k.multiply(v)).modPow(a.add(u.multiply(x)), N);
        System.err.println("Computed S=" + S.toString(16));
        byte[] s = new byte[32];
        copyBytes(s, S, 32, 0);
        byte[] s1 = new byte[16];
        byte[] s2 = new byte[16];
        for (int i = 0; i < 16; i++) {
            s1[i] = s[i * 2];
            s2[i] = s[i * 2 + 1];
        }
        h.reset();
        h.update(s1);
        s1 = h.digest();
        h.reset();
        h.update(s2);
        s2 = h.digest();
        s = new byte[40];
        for (int i = 0; i < 20; i++) {
            s[i * 2] = s1[i];
            s[i * 2 + 1] = s2[i];
        }
        sessKey = s;
        System.err.println("Session key=" + getBigInteger(sessKey).toString(16));
        h.reset();
        h.update(Nb);
        byte[] ngh = h.digest();
        h.reset();
        h.update(gb);
        byte[] gh = h.digest();
        for (int i = 0; i < 20; i++) ngh[i] ^= gh[i];
        h.reset();
        h.update(ngh);
        h.update(userHash);
        h.update(salt);
        h.update(getBytesOf(A));
        h.update(getBytesOf(B));
        h.update(sessKey);
        byte[] m1 = h.digest();
        System.err.println("M1=" + getBigInteger(m1).toString(16));
        h.reset();
        h.update(getBytesOf(A));
        h.update(m1);
        h.update(sessKey);
        authM2 = h.digest();
        System.err.println("M2=" + getBigInteger(authM2).toString(16));
        byte b[] = new byte[75];
        b[0] = LOGON_PROOF;
        copyBytes(b, A, 32, 1);
        copyBytes(b, m1, 20, 33, 0);
        b[73] = 0;
        b[74] = (byte) unk12;
        return writeByte(b);
    }
}
