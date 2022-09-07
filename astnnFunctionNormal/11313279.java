class BackupThread extends Thread {
    public void read(RandomAccessFile raf, DirectByteBuffer buffer, long offset) throws FMFileManagerException {
        if (raf == null) {
            throw new FMFileManagerException("read: raf is null");
        }
        FileChannel fc = raf.getChannel();
        if (!fc.isOpen()) {
            Debug.out("FileChannel is closed: " + owner.getName());
            throw (new FMFileManagerException("read - file is closed"));
        }
        AEThread2.setDebug(owner);
        try {
            fc.position(offset);
            while (fc.position() < fc.size() && buffer.hasRemaining(DirectByteBuffer.SS_FILE)) {
                buffer.read(DirectByteBuffer.SS_FILE, fc);
            }
        } catch (Exception e) {
            Debug.printStackTrace(e);
            throw (new FMFileManagerException("read fails", e));
        }
    }
}
