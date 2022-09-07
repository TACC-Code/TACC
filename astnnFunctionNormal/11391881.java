class BackupThread extends Thread {
    public void truncate(String path, long truncate_length, Thunk2<Integer, Update> app_cb) {
        if (logger.isInfoEnabled()) logger.info("truncate - start: file=" + path + " length=" + truncate_length);
        ObjectState state = state_map.get(path);
        if (state == null) {
            logger.warn("truncate - done, file not cached: path=" + path);
            int status = MoxieStatus.MOXIE_STATUS_IO;
            app_cb.run(status, (Update) null);
            return;
        }
        synchronized (state) {
            state.busy = true;
        }
        boolean requires_server_update = false;
        try {
            long file_length = state.getChannel().size();
            if (file_length > truncate_length) {
                state.getChannel().truncate(truncate_length);
                requires_server_update = true;
                state.md.size = (int) truncate_length;
            } else if (file_length < truncate_length) {
                BUG("TODO");
                requires_server_update = true;
                state.md.size = (int) truncate_length;
            } else {
                assert (file_length == truncate_length);
            }
        } catch (Exception e) {
            logger.fatal("Exception while truncating file: e=" + e);
            synchronized (state) {
                state.busy = false;
            }
            int status = MoxieStatus.MOXIE_STATUS_IO;
            app_cb.run(status, (Update) null);
            return;
        }
        if (state.open_write) {
            logger.warn("truncate - done, cannot write back changes while" + " other processes have file open: path=" + path);
            truncate_update_cb.run(path, truncate_length, app_cb, (Update) null);
        } else if (!requires_server_update) {
            truncate_update_cb.run(path, truncate_length, app_cb, (Update) null);
        } else {
            assert (requires_server_update);
            FileChannel fchannel = null;
            try {
                fchannel = state.getChannel();
            } catch (IOException e) {
                synchronized (state) {
                    state.busy = false;
                }
                int status = MoxieStatus.MOXIE_STATUS_IO;
                app_cb.run(status, (Update) null);
                return;
            }
            Thunk1<Update> update_cb = curry(truncate_update_cb, path, truncate_length, app_cb);
            updater.computeUpdate(path, state.getCachePath(), state.ver, fchannel, update_cb);
        }
        return;
    }
}
