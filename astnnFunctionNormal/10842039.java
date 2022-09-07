class BackupThread extends Thread {
    public void init(Map attributes) {
        HashMap sha_attr = new HashMap();
        HashMap md5_attr = new HashMap();
        byte[] secret = (byte[]) attributes.get(SECRET);
        if (secret != null) {
            int l = (secret.length >>> 1) + (secret.length & 1);
            byte[] s1 = Util.trim(secret, 0, l);
            byte[] s2 = Util.trim(secret, secret.length - l, l);
            md5_attr.put(IMac.MAC_KEY_MATERIAL, s1);
            sha_attr.put(IMac.MAC_KEY_MATERIAL, s2);
            try {
                hmac_md5.init(md5_attr);
                hmac_sha.init(sha_attr);
            } catch (InvalidKeyException ike) {
                throw new Error(ike.toString());
            }
        } else if (!init) {
            throw new IllegalArgumentException("no secret supplied");
        }
        byte[] seeed = (byte[]) attributes.get(SEED);
        if (seeed != null) {
            seed = (byte[]) seeed.clone();
        } else if (!init) {
            throw new IllegalArgumentException("no seed supplied");
        }
        hmac_md5.update(seed, 0, seed.length);
        md5_a = hmac_md5.digest();
        hmac_md5.reset();
        hmac_sha.update(seed, 0, seed.length);
        sha_a = hmac_sha.digest();
        hmac_sha.reset();
        fillBuffer();
        init = true;
    }
}
