class BackupThread extends Thread {
    public static byte[] computeSHA1FromString(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            byte[] input = digest.digest(s.getBytes("UTF8"));
            return input;
        } catch (NoSuchAlgorithmException e) {
            throw new EkoRuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EkoRuntimeException(e);
        }
    }
}
