class BackupThread extends Thread {
    public static byte[] decodeBase64(String base64) throws Exception {
        InputStream fromBase64 = MimeUtility.decode(new ByteArrayInputStream(base64.getBytes()), "base64");
        byte[] buf = new byte[1024];
        ByteArrayOutputStream toByteArray = new ByteArrayOutputStream();
        for (int len = -1; (len = fromBase64.read(buf)) != -1; ) toByteArray.write(buf, 0, len);
        return toByteArray.toByteArray();
    }
}
