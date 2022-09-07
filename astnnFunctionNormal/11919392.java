class BackupThread extends Thread {
    public static String md5(String senha) throws UnsupportedEncodingException {
        String sen;
        StringBuilder sb = new StringBuilder(32);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes("UTF-8")));
        sen = hash.toString(16);
        int tam = sen.length();
        while (tam < 32) {
            sb.append('0');
            tam++;
        }
        sb.append(sen);
        return sb.toString();
    }
}
