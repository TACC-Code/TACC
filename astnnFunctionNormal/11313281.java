class BackupThread extends Thread {
    public void write(RandomAccessFile raf, DirectByteBuffer[] buffers, long position) throws FMFileManagerException {
        if (raf == null) {
            throw (new FMFileManagerException("write fails: raf is null"));
        }
        FileChannel fc = raf.getChannel();
        if (!fc.isOpen()) {
            Debug.out("FileChannel is closed: " + owner.getName());
            throw (new FMFileManagerException("read - file is closed"));
        }
        AEThread2.setDebug(owner);
        int[] original_positions = null;
        try {
            long expected_write = 0;
            long actual_write = 0;
            boolean partial_write = false;
            if (DEBUG) {
                for (int i = 0; i < buffers.length; i++) {
                    expected_write += buffers[i].limit(DirectByteBuffer.SS_FILE) - buffers[i].position(DirectByteBuffer.SS_FILE);
                }
            }
            fc.position(position);
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
                while (last_bb.position() != last_bb.limit()) {
                    long written = fc.write(bbs);
                    actual_write += written;
                    if (written > 0) {
                        loop = 0;
                        if (DEBUG) {
                            if (last_bb.position() != last_bb.limit()) {
                                partial_write = true;
                                if (DEBUG_VERBOSE) {
                                    Debug.out("FMFile::write: **** partial write **** this = " + written + ", total = " + actual_write + ", target = " + expected_write);
                                }
                            }
                        }
                    } else {
                        loop++;
                        if (loop == WRITE_RETRY_LIMIT) {
                            Debug.out("FMFile::write: zero length write - abandoning");
                            throw (new FMFileManagerException("write fails: retry limit exceeded"));
                        } else {
                            if (DEBUG_VERBOSE) {
                                Debug.out("FMFile::write: zero length write - retrying");
                            }
                            try {
                                Thread.sleep(WRITE_RETRY_DELAY * loop);
                            } catch (InterruptedException e) {
                                throw (new FMFileManagerException("write fails: interrupted"));
                            }
                        }
                    }
                }
            }
            if (DEBUG) {
                if (expected_write != actual_write) {
                    Debug.out("FMFile::write: **** partial write **** failed: expected = " + expected_write + ", actual = " + actual_write);
                    throw (new FMFileManagerException("write fails: expected write/actual write mismatch"));
                } else {
                    if (partial_write && DEBUG_VERBOSE) {
                        Debug.out("FMFile::write: **** partial write **** completed ok");
                    }
                }
            }
        } catch (Throwable e) {
            if (original_positions != null) {
                try {
                    for (int i = 0; i < original_positions.length; i++) {
                        buffers[i].position(DirectByteBuffer.SS_FILE, original_positions[i]);
                    }
                } catch (Throwable e2) {
                    Debug.out(e2);
                }
            }
            throw (new FMFileManagerException("write fails", e));
        }
    }
}
