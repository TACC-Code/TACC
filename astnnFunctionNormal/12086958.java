class BackupThread extends Thread {
    private NIOFileAdapter(String filename, boolean lockFile, long initialLength, boolean readOnly, int pageSize, int lruLimit) throws Db4oIOException {
        _pageSize = pageSize;
        try {
            _file = new RandomAccessFile(filename, readOnly ? "r" : "rw");
            _channel = _file.getChannel();
            _size = _channel.size();
        } catch (IOException e) {
            throw new Db4oIOException(e);
        }
        _page = null;
        _pageId = 0;
        _position = 0;
        _dirty = false;
        _id2Page = new HashMap();
        _lruPages = new LinkedList();
        _lruLimit = lruLimit;
    }
}
