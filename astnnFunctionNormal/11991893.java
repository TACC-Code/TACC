class BackupThread extends Thread {
    private String getValidationCode(User user) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] msg = Double.valueOf(user.getJoined().getTime() + 43243242).toString().getBytes();
        md.update(msg);
        byte[] digest = md.digest();
        String code = "";
        for (int i = 0; i < digest.length; i++) code += Byte.toString(digest[i]);
        return code;
    }
}
