class BackupThread extends Thread {
    protected final void load(File file) {
        try {
            final FileChannel channel = new FileInputStream(file).getChannel();
            final long bytes = channel.size();
            if (Bits.CFG_BITS_MAP.getValue()) buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, bytes).order(ByteOrder.LITTLE_ENDIAN); else {
                allocate((int) bytes);
                channel.read(buffer);
            }
            channel.close();
        } catch (FileNotFoundException e) {
            Log.getInstance().severe("File " + file + " not found: " + e.getMessage());
        } catch (IOException e) {
            Log.getInstance().severe("IO Exception on " + file + ": " + e.getMessage());
        }
    }
}
