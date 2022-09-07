class BackupThread extends Thread {
    private synchronized void fillBuffer() {
        int len = hmac_md5.macSize();
        for (int i = 0; i < buffer.length; i += len) {
            hmac_md5.update(md5_a, 0, md5_a.length);
            hmac_md5.update(seed, 0, seed.length);
            byte[] b = hmac_md5.digest();
            hmac_md5.reset();
            System.arraycopy(b, 0, buffer, i, len);
            hmac_md5.update(md5_a, 0, md5_a.length);
            md5_a = hmac_md5.digest();
            hmac_md5.reset();
        }
        len = hmac_sha.macSize();
        for (int i = 0; i < buffer.length; i += len) {
            hmac_sha.update(sha_a, 0, sha_a.length);
            hmac_sha.update(seed, 0, seed.length);
            byte[] b = hmac_sha.digest();
            hmac_sha.reset();
            for (int j = 0; j < len; j++) {
                buffer[j + i] ^= b[j];
            }
            hmac_sha.update(sha_a, 0, sha_a.length);
            sha_a = hmac_sha.digest();
            hmac_sha.reset();
        }
        idx = 0;
    }
}
