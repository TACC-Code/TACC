class BackupThread extends Thread {
    public void fsync(String path, long fhandle, Thunk2<Integer, Update> app_cb) {
        if (logger.isInfoEnabled()) logger.info("fsync - start: path=" + path + " fhandle=" + fhandle);
        ObjectState state = state_map.get(path);
        assert (state != null);
        synchronized (state) {
            state.busy = true;
        }
        FileChannel fchannel = null;
        try {
            fchannel = state.getChannel();
        } catch (IOException e) {
            int status = MoxieStatus.MOXIE_STATUS_IO;
            app_cb.run(status, (Update) null);
            return;
        }
        Thunk1<Update> update_cb = curry(fsync_update_cb, path, fhandle, app_cb);
        updater.computeUpdate(path, state.getCachePath(), state.ver, fchannel, update_cb);
        return;
    }
}
