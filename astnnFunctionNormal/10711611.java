class BackupThread extends Thread {
    protected final void save(File file) {
        buffer = buffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
        try {
            FileChannel channel = new FileOutputStream(file).getChannel();
            channel.write(buffer);
            channel.close();
        } catch (FileNotFoundException e) {
            Log.getInstance().severe("File " + file + " not found: " + e.getMessage());
        } catch (IOException e) {
            Log.getInstance().severe("IO Exception on " + file + ": " + e.getMessage());
        }
    }
}
