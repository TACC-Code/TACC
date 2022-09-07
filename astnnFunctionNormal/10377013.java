class BackupThread extends Thread {
    private void mapFile() throws IOException {
        long length = getLength();
        if (length > 0) {
            FileChannel.MapMode mapMode = readOnly ? FileChannel.MapMode.READ_ONLY : FileChannel.MapMode.READ_WRITE;
            byteBuffer = file.getChannel().map(mapMode, 0, length);
        }
    }
}
