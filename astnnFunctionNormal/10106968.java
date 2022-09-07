class BackupThread extends Thread {
    public static String md5(HttpResponseData httpResponse) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(httpResponse.getResponseAsBytes());
        byte[] bytes = messageDigest.digest();
        return byteArrayToHexString(bytes);
    }
}
