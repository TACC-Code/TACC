class BackupThread extends Thread {
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int r; (r = in.read(buf)) != -1; bout.write(buf, 0, r)) ;
        return bout.toByteArray();
    }
}
