class BackupThread extends Thread {
    public void write(String path, long fh, ByteBuffer buffer, long offset, Thunk1<FuseException> cb) {
        String fullpath = getPath(path);
        int length = buffer.capacity();
        logger.info("write: path=" + fullpath + " fh=" + fh + " offset=" + offset + " length=" + length);
        File f = new File(fullpath);
        if (!f.exists()) {
            FuseException fe = new FuseException("Does not exist.");
            fe.initErrno(FuseException.ENOENT);
            cb.run(fe);
            return;
        }
        if (!f.canWrite()) {
            FuseException fe = new FuseException("Not writeable.");
            fe.initErrno(FuseException.EINVAL);
            cb.run(fe);
            return;
        }
        try {
            FileChannel fchannel = (new FileOutputStream(f)).getChannel();
            fchannel.position(offset);
            fchannel.write(buffer);
        } catch (Exception e) {
            FuseException fe = new FuseException("Write failed.");
            fe.initErrno(FuseException.EACCES);
            cb.run(fe);
            return;
        }
        FuseException fe = null;
        cb.run(fe);
        return;
    }
}
