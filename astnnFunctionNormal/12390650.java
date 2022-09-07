class BackupThread extends Thread {
    public static byte[] getBytesContentFromJar(String s) throws IOException {
        InputStream is = Statics.class.getResourceAsStream(s);
        ByteArrayOutputStream bab = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) != -1) bab.write(buff, 0, len);
        return bab.toByteArray();
    }
}
