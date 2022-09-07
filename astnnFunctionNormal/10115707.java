class BackupThread extends Thread {
    public static byte[] addressHash(byte[] pubkeyBytes) {
        try {
            byte[] sha256 = MessageDigest.getInstance(SHA256).digest(pubkeyBytes);
            RIPEMD160Digest digest = new RIPEMD160Digest();
            digest.update(sha256, 0, sha256.length);
            byte[] out = new byte[20];
            digest.doFinal(out, 0);
            return out;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
