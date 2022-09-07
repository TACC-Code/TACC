class BackupThread extends Thread {
    public static byte[] digest(InputStream inputStream, String algorithm) throws IOException, NoSuchAlgorithmException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int i;
        while ((i = bufferedInputStream.read()) > -1) {
            messageDigest.update((byte) i);
        }
        bufferedInputStream.close();
        return messageDigest.digest();
    }
}
