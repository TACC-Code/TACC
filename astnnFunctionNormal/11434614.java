class BackupThread extends Thread {
    public static String createId() {
        String id = "";
        try {
            if (prng == null) {
                prng = SecureRandom.getInstance("SHA1PRNG");
            }
            String randomNum = new Integer(prng.nextInt()).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            id = hexEncode(result);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return id;
    }
}
