class BackupThread extends Thread {
    public void read(RandomAccessFile raf, DirectByteBuffer[] buffers, long offset) throws FMFileManagerException {
        if (raf == null) {
            throw new FMFileManagerException("read: raf is null");
        }
        FileChannel fc = raf.getChannel();
        if (!fc.isOpen()) {
            Debug.out("FileChannel is closed: " + owner.getName());
            throw (new FMFileManagerException("read - file is closed"));
        }
        AEThread2.setDebug(owner);
        int[] original_positions = null;
        long read_start = SystemTime.getHighPrecisionCounter();
        try {
            fc.position(offset);
            ByteBuffer[] bbs = new ByteBuffer[buffers.length];
            original_positions = new int[buffers.length];
            ByteBuffer last_bb = null;
            for (int i = 0; i < bbs.length; i++) {
                ByteBuffer bb = bbs[i] = buffers[i].getBuffer(DirectByteBuffer.SS_FILE);
                int pos = original_positions[i] = bb.position();
                if (pos != bb.limit()) {
                    last_bb = bbs[i];
                }
            }
            if (last_bb != null) {
                int loop = 0;
                while (fc.position() < fc.size() && last_bb.hasRemaining()) {
                    long read = fc.read(bbs);
                    if (read > 0) {
                        loop = 0;
                    } else {
                        loop++;
                        if (loop == READ_RETRY_LIMIT) {
                            Debug.out("FMFile::read: zero length read - abandoning");
                            throw (new FMFileManagerException("read fails: retry limit exceeded"));
                        } else {
                            if (DEBUG_VERBOSE) {
                                Debug.out("FMFile::read: zero length read - retrying");
                            }
                            try {
                                Thread.sleep(READ_RETRY_DELAY * loop);
                            } catch (InterruptedException e) {
                                throw (new FMFileManagerException("read fails: interrupted"));
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            try {
                Debug.out("Read failed: " + owner.getString() + ": raf open=" + raf.getChannel().isOpen() + ", len=" + raf.length() + ",off=" + offset);
            } catch (IOException f) {
            }
            Debug.printStackTrace(e);
            if (original_positions != null) {
                try {
                    for (int i = 0; i < original_positions.length; i++) {
                        buffers[i].position(DirectByteBuffer.SS_FILE, original_positions[i]);
                    }
                } catch (Throwable e2) {
                    Debug.out(e2);
                }
            }
            throw (new FMFileManagerException("read fails", e));
        } finally {
            long elapsed_millis = (SystemTime.getHighPrecisionCounter() - read_start) / 1000000;
            if (elapsed_millis > 10 * 1000) {
                System.out.println("read took " + elapsed_millis + " for " + owner.getString());
            }
        }
    }
}
