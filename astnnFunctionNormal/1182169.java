class BackupThread extends Thread {
    private void mapBuffer(String databaseFile) throws IOException {
        long len = new File(databaseFile).length();
        buffer = new RandomAccessFile(databaseFile, "rw").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, len);
    }
}
