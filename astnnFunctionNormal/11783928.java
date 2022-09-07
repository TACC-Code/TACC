class BackupThread extends Thread {
    public static boolean verifyTopResponse(String topParams, String topSession, String topSign, String appKey, String appSecret) throws IOException {
        StringBuilder result = new StringBuilder();
        MessageDigest md5 = getMd5MessageDigest();
        result.append(appKey).append(topParams).append(topSession).append(appSecret);
        byte[] bytes = md5.digest(result.toString().getBytes(Constants.CHARSET_UTF8));
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes).equals(topSign);
    }
}
