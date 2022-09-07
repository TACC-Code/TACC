class BackupThread extends Thread {
    public static String stringMessageDigest(String content, String algorithm) {
        if (content == null) {
            return "";
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmRuntimeException(e);
        }
        byte[] digestBytes = digest.digest(content.getBytes());
        return Base64Util.encode(digestBytes);
    }
}
