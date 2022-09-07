class BackupThread extends Thread {
    public InputStreamXReader(final File in, final CharsetDecoder decoder) throws IOException {
        this(new FileInputStream(in).getChannel(), decoder);
        setByteBufferCapacity0((int) Math.min(in.length(), InputStreamXReader.DEFAULT_INPUT_BUFFER_CAPACITY));
    }
}
