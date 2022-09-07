class BackupThread extends Thread {
    public static String getMD5(String s) {
        StringBuffer out = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] b = s.getBytes();
            b = md.digest(b);
            for (int i = 0; i < b.length; i++) {
                out.append((int) b[i]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}
