class BackupThread extends Thread {
    public static byte[] computeDigest(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            ServiceManager.log(e);
            byte[] data = new byte[4];
            data[0] = 0;
            data[1] = 0;
            data[2] = 0;
            data[3] = 0;
            return data;
        }
        byte[] data = new byte[str.length() << 1];
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            data[(i << 1)] = (byte) (ch & 0xFF);
            data[(i << 1) + 1] = (byte) ((ch >> 8) & 0xFF);
        }
        md.update(data);
        return md.digest();
    }
}
