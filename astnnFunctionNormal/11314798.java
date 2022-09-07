class BackupThread extends Thread {
    public ByteBuffer getMD5Digest() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new ByteBuffer(md.digest(getBytes()), false);
        } catch (NoSuchAlgorithmException e) {
            throw new ADVRuntimeException(e);
        }
    }
}
