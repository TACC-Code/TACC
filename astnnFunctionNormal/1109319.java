class BackupThread extends Thread {
    private static byte[] deriveKeyPKCS5(char[] password, byte[] salt, int iterations, int dklen, String digAlg) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digAlg);
            byte[] pass = new byte[password.length];
            for (int i = 0; i < password.length; i++) pass[i] = (byte) (password[i] & 0xff);
            digest.update(pass);
            digest.update(salt);
            byte[] dig = digest.digest();
            int len = dig.length;
            for (int i = 1; i < iterations; i++) {
                digest.update(dig);
                digest.digest(dig, 0, len);
            }
            byte[] ret = new byte[dklen];
            System.arraycopy(dig, 0, ret, 0, dklen);
            return ret;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
