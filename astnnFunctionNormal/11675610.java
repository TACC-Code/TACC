class BackupThread extends Thread {
    public static String encode(String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(in.getBytes());
            return new String(digest);
        } catch (Exception e) {
            throw new MelatiBugMelatiException("For some reason I couldn't encode the password!", e);
        }
    }
}
