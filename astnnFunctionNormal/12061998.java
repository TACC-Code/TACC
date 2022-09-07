class BackupThread extends Thread {
    private String stringToMD5(String str) {
        try {
            byte b[] = str.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            byte[] digest = md.digest();
            return getHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.WARNING, "md5 error", e);
        }
        return null;
    }
}
