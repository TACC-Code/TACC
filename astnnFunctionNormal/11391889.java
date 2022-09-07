class BackupThread extends Thread {
    public void insert(String path, byte[] data, int offset, int length) {
        if (logger.isDebugEnabled()) logger.debug("Inserting in cache: path=" + path + " data.length=" + data.length + " offset=" + offset + " length=" + length);
        ObjectState state = state_map.get(path);
        assert (state != null);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            state.getChannel().write(buffer, offset);
        } catch (Exception e) {
            BUG("TODO: Return I/O error.  " + e);
        }
        return;
    }
}
