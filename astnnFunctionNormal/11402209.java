class BackupThread extends Thread {
    public static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (Exception e) {
            throw new JacumCmsException("Error calculating hash", e);
        }
    }
}
