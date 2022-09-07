class BackupThread extends Thread {
    private byte[] stringToKey(char[] secret, byte[] opaque) throws GeneralSecurityException {
        if (opaque != null && opaque.length > 0) {
            throw new RuntimeException("Invalid parameter to stringToKey");
        }
        byte[] passwd = null;
        byte[] digest = null;
        try {
            passwd = charToUtf16(secret);
            MessageDigest md = sun.security.provider.MD4.getInstance();
            md.update(passwd);
            digest = md.digest();
        } catch (Exception e) {
            return null;
        } finally {
            if (passwd != null) {
                Arrays.fill(passwd, (byte) 0);
            }
        }
        return digest;
    }
}
