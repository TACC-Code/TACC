class BackupThread extends Thread {
    public static String MD5Encoder(InputStream is) {
        if (null == is) return null;
        init();
        byte[] rb = new byte[4096];
        try {
            int size = is.read(rb);
            while (0 < size) {
                md5.update(rb, 0, size);
                size = is.read(rb);
            }
            return StringUtils.byteArrayToHexString(md5.digest());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
