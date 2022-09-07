class BackupThread extends Thread {
    static byte[] crypt(String password, char salt) {
        byte b[] = password.getBytes();
        byte a[] = new byte[2 + b.length];
        a[0] = (byte) (salt >> 8);
        a[1] = (byte) (salt & 0xff);
        System.arraycopy(b, 0, a, 2, b.length);
        try {
            b = MessageDigest.getInstance("MD5").digest(a);
            byte c[] = new byte[2 + b.length];
            System.arraycopy(a, 0, c, 0, 2);
            System.arraycopy(b, 0, c, 2, b.length);
            return c;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
