class BackupThread extends Thread {
    private static String md5(String data) {
        byte[] bdata = new byte[data.length()];
        int i;
        byte[] hash;
        for (i = 0; i < data.length(); i++) bdata[i] = (byte) (data.charAt(i) & 0xff);
        try {
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            hash = md5er.digest(bdata);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        StringBuffer r = new StringBuffer(32);
        for (i = 0; i < hash.length; i++) {
            String x = Integer.toHexString(hash[i] & 0xff);
            if (x.length() < 2) r.append("0");
            r.append(x);
        }
        return r.toString();
    }
}
