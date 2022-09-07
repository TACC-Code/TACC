class BackupThread extends Thread {
    public OutputStream getOutputStream(boolean bAppend) throws FileSystemException {
        if (getType() != FileType.IMAGINARY && !getType().hasContent()) {
            throw new FileSystemException("vfs.provider/write-not-file.error", name);
        }
        if (!isWriteable()) {
            throw new FileSystemException("vfs.provider/write-read-only.error", name);
        }
        if (bAppend && !getFileSystem().hasCapability(Capability.APPEND_CONTENT)) {
            throw new FileSystemException("vfs.provider/write-append-not-supported.error", name);
        }
        if (getType() == FileType.IMAGINARY) {
            FileObject parent = getParent();
            if (parent != null) {
                parent.createFolder();
            }
        }
        try {
            return doGetOutputStream(bAppend);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exc) {
            throw new FileSystemException("vfs.provider/write.error", new Object[] { name }, exc);
        }
    }
}
