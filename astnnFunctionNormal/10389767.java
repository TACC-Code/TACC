class BackupThread extends Thread {
    public void read(File src, OutputStream dest) {
        FileChannel channel = null;
        int bytesRead = -1;
        try {
            channel = new FileInputStream(src).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while ((bytesRead = channel.read(buffer)) != -1) {
                buffer.flip();
                dest.write(buffer.array(), 0, bytesRead);
                buffer.clear();
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            try {
                if (dest != null) {
                    dest.flush();
                    dest.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
