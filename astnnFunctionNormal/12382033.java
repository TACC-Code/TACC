class BackupThread extends Thread {
    private byte[] messageDigest(String string, MessageDigest digester) throws Exception {
        byte md[] = new byte[8192];
        StringBuffer StringBuffer1 = new StringBuffer(string);
        ByteArrayInputStream bis1 = new ByteArrayInputStream(StringBuffer1.toString().getBytes("UTF-8"));
        InputStream in = bis1;
        for (int n = 0; (n = in.read(md)) > -1; ) digester.update(md, 0, n);
        return digester.digest();
    }
}
