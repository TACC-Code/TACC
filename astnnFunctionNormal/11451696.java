class BackupThread extends Thread {
    protected void openFile(File file) throws IOException {
        _channel = new FileInputStream(file).getChannel();
        _buffer = Charset.forName("ISO-8859-15").newDecoder().decode(_channel.map(FileChannel.MapMode.READ_ONLY, 0, _channel.size()));
    }
}
