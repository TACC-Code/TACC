class BackupThread extends Thread {
    public static String calculateMD5(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        is.close();
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
