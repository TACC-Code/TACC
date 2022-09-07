class BackupThread extends Thread {
    public static String MD5Password(String password) {
        StringBuilder digest = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes("ASCII"));
            for (byte b : hash) {
                digest.append(String.format(EnUsCulture, "%02x", b));
            }
            return "$1$" + digest.toString();
        } catch (Exception e) {
        }
        return EmptyString;
    }
}
