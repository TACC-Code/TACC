class BackupThread extends Thread {
    private byte[] computeClassDigest(ByteBuffer def) {
        try {
            MessageDigest alg = MessageDigest.getInstance(DIGEST_ALGORITHM);
            def.mark();
            alg.update(def);
            def.reset();
            return alg.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException(e);
        }
    }
}
