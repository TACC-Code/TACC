class BackupThread extends Thread {
    public static String sha1(String s) {
        try {
            return byteArrayToHexString(MessageDigest.getInstance("SHA").digest(s.getBytes("US-ASCII")));
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }
}
