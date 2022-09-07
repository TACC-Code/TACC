class BackupThread extends Thread {
    public static byte[] messageDigestForTest(String string, String algorithm) throws TechnicalException {
        MessageDigest msgTest = null;
        byte[] bytes = null;
        try {
            msgTest = MessageDigest.getInstance(algorithm);
            bytes = string.getBytes(Charset.UTF8);
        } catch (NoSuchAlgorithmException e) {
            throw new TechnicalException(e);
        } catch (UnsupportedEncodingException e) {
            throw new TechnicalEncodingException(e);
        }
        msgTest.reset();
        return msgTest.digest(bytes);
    }
}
