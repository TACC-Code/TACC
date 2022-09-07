class BackupThread extends Thread {
    public static String passwordHash(String passwordToHash) {
        MessageDigest digest = null;
        byte hash[] = null;
        byte ch = 0x00;
        int i = 0;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Java libraries don't support SHA-256");
        }
        try {
            hash = digest.digest(passwordToHash.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new Error("Java libraries don't support UTF8");
        }
        if (hash == null || hash.length <= 0) {
            return null;
        }
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
        StringBuffer out = new StringBuffer(hash.length * 2);
        while (i < hash.length) {
            ch = (byte) (hash[i] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(pseudo[(int) ch]);
            ch = (byte) (hash[i] & 0x0F);
            out.append(pseudo[(int) ch]);
            i++;
        }
        String rslt = new String(out);
        return rslt;
    }
}
