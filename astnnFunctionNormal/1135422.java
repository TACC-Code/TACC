class BackupThread extends Thread {
    public void init(String password) {
        clearAll();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        byte[] registerInitBytes = md.digest(password.getBytes());
        init(registerInitBytes);
    }
}
