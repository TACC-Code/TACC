class BackupThread extends Thread {
    private static void screenshot(IOpenGL gl, int width, int height, File file) {
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            FileChannel ch = out.getChannel();
            int fileLength = TARGA_HEADER_SIZE + width * height * 3;
            out.setLength(fileLength);
            MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
            image.put(0, (byte) 0).put(1, (byte) 0);
            image.put(2, (byte) 2);
            image.put(12, (byte) (width & 0xFF));
            image.put(13, (byte) (width >> 8));
            image.put(14, (byte) (height & 0xFF));
            image.put(15, (byte) (height >> 8));
            image.put(16, (byte) 24);
            image.position(TARGA_HEADER_SIZE);
            ByteBuffer bgr = image.slice();
            gl.readPixels(0, 0, width, height, bgr);
            ch.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
