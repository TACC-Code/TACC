class BackupThread extends Thread {
    public static String computeSHA1(String in) {
        String out = "";
        if (in != null || "".equals(in)) {
            byte[] textPassword = in.getBytes();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(textPassword);
                byte[] encryptedPassword = md.digest();
                for (Byte b : encryptedPassword) {
                    out += Integer.toHexString(b & 0xff);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return out;
    }
}
