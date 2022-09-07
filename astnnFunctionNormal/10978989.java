class BackupThread extends Thread {
    private static String getDigestString(MessageDigest digest, File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        byte[] buf = new byte[2048];
        int n;
        while ((n = fin.read(buf)) >= 0) {
            digest.update(buf, 0, n);
        }
        buf = digest.digest();
        fin.close();
        StringBuffer sb = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            byte b = buf[i];
            sb.append(Character.forDigit((b >> 4) & 0xf, 16));
            sb.append(Character.forDigit(b & 0xf, 16));
        }
        return sb.toString();
    }
}
