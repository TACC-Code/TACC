class BackupThread extends Thread {
    public void read(String path, long fhandle, ByteBuffer buffer, long offset, Thunk1<Integer> app_cb) {
        int length = buffer.capacity();
        if (logger.isInfoEnabled()) logger.info("Reading cached file: file=" + path + " offset=" + offset + " length=" + length);
        ObjectState state = state_map.get(path);
        assert (state != null);
        int status = MoxieStatus.MOXIE_STATUS_OK;
        try {
            state.getChannel().read(buffer, offset);
        } catch (Exception e) {
            status = MoxieStatus.MOXIE_STATUS_IO;
        }
        app_cb.run(status);
        return;
    }
}
