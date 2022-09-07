class BackupThread extends Thread {
    public S3ByteArrayObject(String key, byte[] data, int offset, int length, MediaType mediaType) {
        super(key, mediaType);
        MessageDigest md;
        _data = data;
        _offset = offset;
        _length = length;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException nsa) {
            throw new RuntimeException(nsa);
        }
        md.update(_data, _offset, _length);
        _md5 = md.digest();
    }
}
