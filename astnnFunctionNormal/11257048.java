class BackupThread extends Thread {
    private void openExisting(String path) throws IOException {
        _channel = new RandomAccessFile(path, "r").getChannel();
        _channelSize = _channel.size();
        _mode = FileChannel.MapMode.READ_ONLY;
        long len = Math.min((long) _segmentSize, _channelSize);
        _mappedByteBuffer = _channel.map(_mode, 0, (int) len);
    }
}
