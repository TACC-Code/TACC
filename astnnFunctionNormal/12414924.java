class BackupThread extends Thread {
    public static void screenshot(File file, int width, int height) {
        if (GLContext.getCapabilities().OpenGL12) {
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
                tr = new TileRenderer();
                tr.setTileSize(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0);
                tr.setImageSize(width, height);
                tr.setImageBuffer(GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE, bgr);
                GameBox.game.draw();
                doingTiledRender = true;
                do {
                    tr.beginTile();
                    GameBox.game.draw();
                } while (tr.endTile());
                ch.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doingTiledRender = false;
            tr = null;
        } else {
            Console.error("Cannot make screenshot, requires OpenGL 1.2");
        }
    }
}
