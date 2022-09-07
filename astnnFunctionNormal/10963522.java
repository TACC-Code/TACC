class BackupThread extends Thread {
    public static void writeBytesToFile(InputStream is, File out) throws IOException {
        InputStream in = new BufferedInputStream(is);
        ByteArrayOutputStream of = new ByteArrayOutputStream();
        RandomAccessFile file = new RandomAccessFile(out, "rw");
        byte[] buffer = new byte[4096];
        for (int read = 0; (read = in.read(buffer)) != -1; of.write(buffer, 0, read)) ;
        file.write(of.toByteArray());
        file.close();
    }
}
