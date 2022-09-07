class BackupThread extends Thread {
    public static byte[] readAll(InputStream input) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] temp = new byte[7];
        int r;
        while ((r = input.read(temp)) != -1) buf.write(temp, 0, r);
        return buf.toByteArray();
    }
}
