class BackupThread extends Thread {
    private static boolean contentEquals(RandomAccessFile f, byte[] a) throws IOException {
        return f.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, DATA_LENGTH).equals(ByteBuffer.wrap(a));
    }
}
