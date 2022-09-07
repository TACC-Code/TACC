class BackupThread extends Thread {
    private Directory readDir(String path) {
        ObjectState state = state_map.get(path);
        assert (state != null);
        String cache_path = state.getCachePath();
        Directory dir = null;
        try {
            File f = new File(cache_path);
            int dir_size = (int) f.length();
            ByteBuffer buffer = ByteBuffer.allocate(dir_size);
            FileChannel fchannel = state.getChannel();
            fchannel.position(0);
            while (buffer.hasRemaining()) {
                int bytes_read = fchannel.read(buffer);
                if (bytes_read == -1) {
                    break;
                }
            }
            byte[] dir_bytes = buffer.array();
            ByteArrayInputBuffer byte_buffer = new ByteArrayInputBuffer(dir_bytes);
            dir = (Directory) byte_buffer.nextObject();
        } catch (IOException e) {
            logger.warn("Failed to read directory from cache: path=" + path);
            dir = (Directory) null;
        } catch (QSException e) {
            BUG("Failed to reconstruct dir. " + e);
        }
        return dir;
    }
}
