class BackupThread extends Thread {
    public SAAJDataSource(InputStream ss, int maxCached, String contentType, boolean readall) throws java.io.IOException {
        if (ss instanceof BufferedInputStream) {
            this.ss = ss;
        } else {
            this.ss = new BufferedInputStream(ss);
        }
        this.maxCached = maxCached;
        if ((null != contentType) && (contentType.length() != 0)) {
            this.contentType = contentType;
        }
        if (maxCached < MIN_MEMORY_DISK_CACHED) {
            throw new IllegalArgumentException("badMaxCached " + maxCached);
        }
        if (readall) {
            byte[] readbuffer = new byte[READ_CHUNK_SZ];
            int read = 0;
            do {
                read = ss.read(readbuffer);
                if (read > 0) {
                    writeToMemory(readbuffer, read);
                }
            } while (read > -1);
            close();
        }
    }
}
