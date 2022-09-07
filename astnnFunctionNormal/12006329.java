class BackupThread extends Thread {
    public void read(String path, long fh, ByteBuffer buffer, long offset, Thunk1<FuseException> cb) {
        String fullpath = getPath(path);
        int length = buffer.capacity();
        logger.info("read: path=" + fullpath + " fh=" + fh + " offset=" + offset + " length=" + length);
        File f = new File(fullpath);
        if (!f.exists()) {
            FuseException fe = new FuseException("Does not exist.");
            fe.initErrno(FuseException.ENOENT);
            cb.run(fe);
            return;
        }
        if (!f.canRead()) {
            FuseException fe = new FuseException("Not readable.");
            fe.initErrno(FuseException.EINVAL);
            cb.run(fe);
            return;
        }
        try {
            FileChannel fchannel = (new FileInputStream(f)).getChannel();
            fchannel.position(offset);
            fchannel.read(buffer);
        } catch (Exception e) {
            FuseException fe = new FuseException("Read failed.");
            fe.initErrno(FuseException.EACCES);
            cb.run(fe);
            return;
        }
        FuseException fe = null;
        cb.run(fe);
        return;
    }
}
