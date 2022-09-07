class BackupThread extends Thread {
    public RandomAccessContent getRandomAccessContent(final RandomAccessMode mode) throws FileSystemException {
        if (!getType().hasContent()) {
            throw new FileSystemException("vfs.provider/read-not-file.error", name);
        }
        if (mode.requestRead()) {
            if (!getFileSystem().hasCapability(Capability.RANDOM_ACCESS_READ)) {
                throw new FileSystemException("vfs.provider/random-access-read-not-supported.error");
            }
            if (!isReadable()) {
                throw new FileSystemException("vfs.provider/read-not-readable.error", name);
            }
        }
        if (mode.requestWrite()) {
            if (!getFileSystem().hasCapability(Capability.RANDOM_ACCESS_WRITE)) {
                throw new FileSystemException("vfs.provider/random-access-write-not-supported.error");
            }
            if (!isWriteable()) {
                throw new FileSystemException("vfs.provider/write-read-only.error", name);
            }
        }
        try {
            return doGetRandomAccessContent(mode);
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/random-access.error", name, exc);
        }
    }
}
