class BackupThread extends Thread {
    public static String encodeToMD5(String s) throws Exception {
        if (s == null) return null;
        String digstr = "";
        MessageDigest MD = MessageDigest.getInstance("MD5");
        byte[] oldbyte = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            oldbyte[i] = (byte) s.charAt(i);
        }
        MD.update(oldbyte);
        byte[] newbyte = MD.digest(oldbyte);
        for (int i = 0; i < newbyte.length; i++) {
            digstr = digstr + newbyte[i];
        }
        return digstr;
    }
}
