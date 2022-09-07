class BackupThread extends Thread {
    public byte[] digest() {
        if (ipadHash == null) {
            throw new IllegalStateException("HMAC not initialised");
        }
        byte[] out = underlyingHash.digest();
        underlyingHash = (IMessageDigest) opadHash.clone();
        underlyingHash.update(out, 0, macSize);
        out = underlyingHash.digest();
        if (truncatedSize == macSize) return out;
        byte[] result = new byte[truncatedSize];
        System.arraycopy(out, 0, result, 0, truncatedSize);
        return result;
    }
}
