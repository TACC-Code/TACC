class BackupThread extends Thread {
    private static String md5sum(final InputStream in, final int size) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        byte[] buf = new byte[size];
        dis.readFully(buf);
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(buf);
        StringBuilder md5StringBuilder = new StringBuilder();
        for (byte b : md5.digest()) {
            md5StringBuilder.append(String.format("%02x", b));
        }
        return md5StringBuilder.toString();
    }
}
