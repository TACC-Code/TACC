class BackupThread extends Thread {
    public static String createCnonce() {
        LOG.trace("enter DigestScheme.createCnonce()");
        String cnonce;
        final String digAlg = "MD5";
        MessageDigest md5Helper;
        try {
            md5Helper = MessageDigest.getInstance(digAlg);
        } catch (NoSuchAlgorithmException e) {
            throw new HttpClientError("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
        }
        cnonce = Long.toString(System.currentTimeMillis());
        cnonce = encode(md5Helper.digest(EncodingUtil.getAsciiBytes(cnonce)));
        return cnonce;
    }
}
