class BackupThread extends Thread {
    public CobolBytes map(long position, int size) throws IOException {
        return new CobolBytes(((FileOutputStream) out).getChannel().map(MapMode.READ_WRITE, position, size).array());
    }
}
