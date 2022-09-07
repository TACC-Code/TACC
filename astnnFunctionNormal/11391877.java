class BackupThread extends Thread {
    public void write(String path, long fhandle, ByteBuffer buffer, long offset, Thunk1<Integer> app_cb) {
        int length = buffer.capacity();
        if (logger.isInfoEnabled()) logger.info("Writing to cached file: file=" + path + " offset=" + offset + " length=" + length);
        ObjectState state = state_map.get(path);
        assert (state != null);
        int status = MoxieStatus.MOXIE_STATUS_OK;
        try {
            int bytes_written = state.getChannel().write(buffer, offset);
            assert (bytes_written == length);
            if (offset + buffer.limit() > state.md.size) state.md.size = (int) (offset + buffer.limit());
        } catch (Exception e) {
            status = MoxieStatus.MOXIE_STATUS_IO;
            logger.fatal("except: e=" + e);
        }
        app_cb.run(status);
        return;
    }
}
