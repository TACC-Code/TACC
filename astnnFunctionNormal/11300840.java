class BackupThread extends Thread {
    public void init(Map attributes) throws InvalidKeyException, IllegalStateException {
        Integer ts = (Integer) attributes.get(TRUNCATED_SIZE);
        truncatedSize = (ts == null ? macSize : ts.intValue());
        if (truncatedSize < (macSize / 2)) {
            throw new IllegalArgumentException("Truncated size too small");
        } else if (truncatedSize < 10) {
            throw new IllegalArgumentException("Truncated size less than 80 bits");
        }
        byte[] K = (byte[]) attributes.get(MAC_KEY_MATERIAL);
        if (K == null) {
            if (ipadHash == null) {
                throw new InvalidKeyException("Null key");
            }
            underlyingHash = (IMessageDigest) ipadHash.clone();
            return;
        }
        Boolean pkcs5 = (Boolean) attributes.get(USE_WITH_PKCS5_V2);
        if (pkcs5 == null) {
            pkcs5 = Boolean.FALSE;
        }
        if (K.length < macSize && !pkcs5.booleanValue()) {
            throw new InvalidKeyException("Key too short");
        }
        if (K.length > blockSize) {
            underlyingHash.update(K, 0, K.length);
            K = underlyingHash.digest();
        }
        if (K.length < blockSize) {
            int limit = (K.length > blockSize) ? blockSize : K.length;
            byte[] newK = new byte[blockSize];
            System.arraycopy(K, 0, newK, 0, limit);
            K = newK;
        }
        underlyingHash.reset();
        opadHash = (IMessageDigest) underlyingHash.clone();
        if (ipad == null) {
            ipad = new byte[blockSize];
        }
        for (int i = 0; i < blockSize; i++) {
            ipad[i] = (byte) (K[i] ^ IPAD_BYTE);
        }
        for (int i = 0; i < blockSize; i++) {
            opadHash.update((byte) (K[i] ^ OPAD_BYTE));
        }
        underlyingHash.update(ipad, 0, blockSize);
        ipadHash = (IMessageDigest) underlyingHash.clone();
        K = null;
    }
}
