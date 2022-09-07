class BackupThread extends Thread {
    private static String encodeSHA1Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(s.getBytes());
            return toHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
