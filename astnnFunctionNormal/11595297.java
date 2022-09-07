class BackupThread extends Thread {
    public static byte[] computePasswordDigest(String str, byte[] key) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            ServiceManager.log(e);
            byte[] data = new byte[16];
            return data;
        }
        byte[] data = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            data[i] = (byte) str.charAt(i);
        }
        md.update(data);
        md.update(key);
        return md.digest();
    }
}
