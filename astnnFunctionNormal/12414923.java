class BackupThread extends Thread {
    public static void screenshot(File file) {
        if (GLContext.getCapabilities().OpenGL12) {
            int w = Display.getDisplayMode().getWidth();
            int h = Display.getDisplayMode().getHeight();
            GameBox.game.draw();
            try {
                RandomAccessFile out = new RandomAccessFile(file, "rw");
                FileChannel ch = out.getChannel();
                int fileLength = TARGA_HEADER_SIZE + w * h * 3;
                out.setLength(fileLength);
                MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
                image.put(0, (byte) 0).put(1, (byte) 0);
                image.put(2, (byte) 2);
                image.put(12, (byte) (w & 0xFF));
                image.put(13, (byte) (w >> 8));
                image.put(14, (byte) (h & 0xFF));
                image.put(15, (byte) (h >> 8));
                image.put(16, (byte) 24);
                image.position(TARGA_HEADER_SIZE);
                ByteBuffer bgr = image.slice();
                GL11.glReadPixels(0, 0, w, h, GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE, bgr);
                ch.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Console.error("Cannot make screenshot, requires OpenGL 1.2");
        }
    }
}
