class BackupThread extends Thread {
    public void release(String path, long fhandle, Thunk2<Integer, Update> app_cb) {
        if (logger.isInfoEnabled()) logger.info("Releasing file: name=" + path + " fhandle=" + fhandle);
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
        if (state.update_in_progress) {
            Thunk1<Update> update_cb = curry(release_update_cb, path, fhandle, app_cb);
            updater.computeUpdate(path, state.getCachePath(), state.ver, fchannel, update_cb);
        } else {
            release_update_cb.run(path, fhandle, app_cb, (Update) null);
        }
        return;
    }
}
