class BackupThread extends Thread {
    public static String getMD5Hash(String inputString) {
        byte buf[] = inputString.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(buf);
            byte[] digest = algorithm.digest();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(pad(Integer.toHexString(0xFF & digest[i]), 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
