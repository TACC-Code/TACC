class BackupThread extends Thread {
    private static byte[] generateKeyFromPasswordSaltWithMD5(byte[] password, byte[] salt, int keyLen) throws IOException {
        if (salt.length < 8) throw new IllegalArgumentException("Salt needs to be at least 8 bytes for key generation.");
        MD5 md5 = new MD5();
        byte[] key = new byte[keyLen];
        byte[] tmp = new byte[md5.getDigestLength()];
        while (true) {
            md5.update(password, 0, password.length);
            md5.update(salt, 0, 8);
            int copy = (keyLen < tmp.length) ? keyLen : tmp.length;
            md5.digest(tmp, 0);
            System.arraycopy(tmp, 0, key, key.length - keyLen, copy);
            keyLen -= copy;
            if (keyLen == 0) return key;
            md5.update(tmp, 0, tmp.length);
        }
    }
}
