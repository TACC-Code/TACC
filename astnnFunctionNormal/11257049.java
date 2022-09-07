class BackupThread extends Thread {
    private void openNew(String path, long flen) throws IOException {
        _channel = new RandomAccessFile(path, "rw").getChannel();
        _channelSize = flen;
        _mode = FileChannel.MapMode.READ_WRITE;
        long len = Math.min((long) _segmentSize, _channelSize);
        _mappedByteBuffer = _channel.map(_mode, 0, (int) len);
    }
}
