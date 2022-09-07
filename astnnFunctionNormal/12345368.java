class BackupThread extends Thread {
    public static String calculateMD5(String formula) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] buffer = formula.getBytes();
        digest.update(buffer);
        byte[] md5sum = digest.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < md5sum.length; i++) {
            int halfbyte = (md5sum[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = md5sum[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        String res = buf.toString();
        return buf.toString();
    }
}
