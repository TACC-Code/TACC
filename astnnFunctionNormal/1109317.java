class BackupThread extends Thread {
    private static byte[] deriveKey(char[] password, int keyLen, byte[] salt, int iterations, int id, String digAlg) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digAlg);
            int u = digest.getDigestLength();
            byte[] Ai = new byte[u];
            byte[] key = new byte[keyLen];
            int v = 64;
            byte[] pb = new byte[password.length * 2 + 2];
            int sl = ((salt.length + v - 1) / v) * v;
            int pl = ((pb.length + v - 1) / v) * v;
            byte[] D = new byte[v];
            byte[] I = new byte[sl + pl];
            byte[] B = new byte[v];
            int i;
            for (i = 0; i < password.length; i++) {
                pb[i * 2] = (byte) (password[i] >>> 8);
                pb[(i * 2) + 1] = (byte) (password[i] & 0xff);
            }
            for (i = 0; i < v; i++) {
                D[i] = (byte) id;
            }
            for (i = 0; i < sl; i++) {
                I[i] = salt[i % salt.length];
            }
            for (i = 0; i < pl; i++) {
                I[sl + i] = pb[i % pb.length];
            }
            int cnt = 0;
            BigInteger one = BigInteger.valueOf(1L);
            byte[] ijRaw = new byte[v];
            while (true) {
                digest.update(D);
                digest.update(I);
                digest.digest(Ai, 0, u);
                for (i = 1; i < iterations; i++) {
                    digest.update(Ai);
                    digest.digest(Ai, 0, u);
                }
                int n = ((u > (keyLen - cnt)) ? keyLen - cnt : u);
                System.arraycopy(Ai, 0, key, cnt, n);
                cnt += n;
                if (cnt >= keyLen) {
                    break;
                }
                for (i = 0; i < v; i++) {
                    B[i] = Ai[i % u];
                }
                BigInteger Bplus1 = (new BigInteger(1, B)).add(one);
                for (i = 0; i < I.length; i += v) {
                    System.arraycopy(I, i, ijRaw, 0, v);
                    BigInteger Ij = new BigInteger(1, ijRaw);
                    Ij = Ij.add(Bplus1);
                    ijRaw = unsignedBigIntToBytes(Ij, v);
                    System.arraycopy(ijRaw, 0, I, i, v);
                }
            }
            return key;
        } catch (Exception e) {
            throw new Error("Error in PKCS12.deriveKey: " + e);
        }
    }
}
