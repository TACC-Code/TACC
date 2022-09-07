class BackupThread extends Thread {
    public static String fileMessageDigest(String fileName, String algorithm) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmRuntimeException(e);
        }
        byte[] digestBytes = digest.digest(InputStreamUtil.getContent(fileName));
        return Base64Util.encode(digestBytes);
    }
}
