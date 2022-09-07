class BackupThread extends Thread {
    public MemoryMappedFile(final File file, final int pageShift, final boolean readOnly, final boolean nativeByteOrder) throws FileNotFoundException {
        this._filepath = file.getAbsolutePath();
        RandomAccessFile raf = new RandomAccessFile(file, readOnly ? "r" : "rw");
        this._channel = raf.getChannel();
        this._readOnly = readOnly;
        this._setAsLittleEndian = nativeByteOrder && (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
        this._pageSize = 1 << pageShift;
        this._oid = System.identityHashCode(this);
    }
}
