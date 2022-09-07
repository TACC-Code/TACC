class BackupThread extends Thread {
    public NIOBuffer(File file) throws IOException {
        super(READONLY, NON_VOLATILE);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        _buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        setGetIndex(0);
        setPutIndex((int) file.length());
        _access = IMMUTABLE;
    }
}
