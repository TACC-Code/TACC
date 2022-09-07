class BackupThread extends Thread {
    public String encodePassword(String username, String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] usernameBuffer = username.getBytes("UTF8");
            byte[] passwordBuffer = password.getBytes("UTF8");
            messageDigest.update(usernameBuffer);
            messageDigest.update(passwordBuffer);
            byte[] digestBuffer = messageDigest.digest();
            String retValue = getMD5String(digestBuffer);
            return retValue;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
