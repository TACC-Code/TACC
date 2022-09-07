class BackupThread extends Thread {
    private static void copyFile(File file, OutputStream os) throws Exception {
        FileChannel c = new RandomAccessFile(file, "r").getChannel();
        MappedByteBuffer b = c.map(MapMode.READ_ONLY, 0, file.length());
        b.load();
        byte[] buf = new byte[1024 * 1024];
        int len = 0;
        while (true) {
            len = Math.min(buf.length, b.remaining());
            b.get(buf, 0, len);
            os.write(buf, 0, len);
            if (b.remaining() == 0) break;
        }
        os.flush();
    }
}
