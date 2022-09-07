class BackupThread extends Thread {
    protected String getMD5Code(String text) {
        String md5 = null;
        try {
            StringBuffer code = new StringBuffer();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte bytes[] = text.getBytes();
            byte digest[] = messageDigest.digest(bytes);
            for (int k = 0; k < digest.length; ++k) {
                code.append(Integer.toHexString(0x0100 + (digest[k] & 0x00FF)).substring(1));
            }
            md5 = code.toString();
        } catch (Exception e) {
            System.out.println("Error convert to md5 password.");
        }
        return md5;
    }
}
