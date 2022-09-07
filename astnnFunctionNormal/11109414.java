class BackupThread extends Thread {
    public HMAC(Digest md, byte[] key, int size) {
        this.md = md;
        this.size = size;
        tmp = new byte[md.getDigestLength()];
        int BLOCKSIZE = 64;
        k_xor_ipad = new byte[BLOCKSIZE];
        k_xor_opad = new byte[BLOCKSIZE];
        if (key.length > BLOCKSIZE) {
            md.reset();
            md.update(key);
            md.digest(tmp);
            key = tmp;
        }
        System.arraycopy(key, 0, k_xor_ipad, 0, key.length);
        System.arraycopy(key, 0, k_xor_opad, 0, key.length);
        for (int i = 0; i < BLOCKSIZE; i++) {
            k_xor_ipad[i] ^= 0x36;
            k_xor_opad[i] ^= 0x5C;
        }
        md.update(k_xor_ipad);
    }
}
