class BackupThread extends Thread {
    public void GL_ScreenShot_f() {
        StringBuffer sb = new StringBuffer(FS.Gamedir() + "/scrshot/jake00.tga");
        FS.CreatePath(sb.toString());
        File file = new File(sb.toString());
        int i = 0;
        int offset = sb.length() - 6;
        while (file.exists() && i++ < 100) {
            sb.setCharAt(offset, (char) ((i / 10) + '0'));
            sb.setCharAt(offset + 1, (char) ((i % 10) + '0'));
            file = new File(sb.toString());
        }
        if (i == 100) {
            VID.Printf(Defines.PRINT_ALL, "Clean up your screenshots\n");
            return;
        }
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            FileChannel ch = out.getChannel();
            int fileLength = TGA_HEADER_SIZE + vid.getWidth() * vid.getHeight() * 3;
            out.setLength(fileLength);
            MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
            image.put(0, (byte) 0).put(1, (byte) 0);
            image.put(2, (byte) 2);
            image.put(12, (byte) (vid.getWidth() & 0xFF));
            image.put(13, (byte) (vid.getWidth() >> 8));
            image.put(14, (byte) (vid.getHeight() & 0xFF));
            image.put(15, (byte) (vid.getHeight() >> 8));
            image.put(16, (byte) 24);
            image.position(TGA_HEADER_SIZE);
            if (vid.getWidth() % 4 != 0) {
                gl.glPixelStorei(GL_PACK_ALIGNMENT, 1);
            }
            if (gl_config.getOpenGLVersion() >= 1.2f) {
                gl.glReadPixels(0, 0, vid.getWidth(), vid.getHeight(), GL_BGR, GL_UNSIGNED_BYTE, image);
            } else {
                gl.glReadPixels(0, 0, vid.getWidth(), vid.getHeight(), GL_RGB, GL_UNSIGNED_BYTE, image);
                byte tmp;
                for (i = TGA_HEADER_SIZE; i < fileLength; i += 3) {
                    tmp = image.get(i);
                    image.put(i, image.get(i + 2));
                    image.put(i + 2, tmp);
                }
            }
            gl.glPixelStorei(GL_PACK_ALIGNMENT, 4);
            ch.close();
        } catch (IOException e) {
            VID.Printf(Defines.PRINT_ALL, e.getMessage() + '\n');
        }
        VID.Printf(Defines.PRINT_ALL, "Wrote " + file + '\n');
    }
}
