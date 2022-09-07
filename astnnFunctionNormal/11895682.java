class BackupThread extends Thread {
    public Mmfile(File file, Mode mode) {
        if (vmIs64Bit()) max_chunks_mapped = MAX_CHUNKS;
        this.mode = mode;
        switch(mode) {
            case CREATE:
                if (file.exists()) verify(file.delete());
                break;
            case OPEN:
                if (!file.canRead() || !file.canWrite()) throw new SuException("can't open " + file);
                break;
            case READ_ONLY:
                if (!file.canRead()) throw new SuException("can't open " + file + " read-only");
                break;
        }
        try {
            fin = new RandomAccessFile(file, mode == Mode.READ_ONLY ? "r" : "rw");
        } catch (FileNotFoundException e) {
            throw new SuException("can't open or create " + file);
        }
        fc = fin.getChannel();
        if (mode != Mode.READ_ONLY) {
            try {
                FileLock lock = fc.tryLock();
                if (lock == null) throw new SuException("can't open " + file);
                lock.release();
            } catch (IOException e1) {
                throw new SuException("io exception locking " + file);
            }
        }
        try {
            file_size = fc.size();
        } catch (IOException e) {
            fatal("can't get database size");
        }
        verify(file_size >= 0);
        if (file_size == 0) {
            set_file_size(file_size = FILEHDR);
            buf(0).put(0, magic);
        } else {
            String err = checkfile();
            if (err != "") throw new SuException("not a valid database file (" + err + ")");
        }
    }
}
