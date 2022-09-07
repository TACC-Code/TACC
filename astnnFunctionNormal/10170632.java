class BackupThread extends Thread {
    public InputStreamXReader(final InputStream in, final CharsetDecoder decoder) {
        super();
        if (in == null) throw new NullPointerException("in");
        if (decoder == null) throw new NullPointerException("decoder");
        this.in = in;
        this.decoder = decoder;
        this.maxCharsPerByte = Ints.max(2, (int) Math.ceil(decoder.maxCharsPerByte()), decoder.replacement().length());
        setCharBufferCapacity0(DEFAULT_OUTPUT_BUFFER_CAPACITY);
        if (in instanceof FileInputStream) this.inChannel = ((FileInputStream) in).getChannel(); else if (in instanceof ReadableByteChannel) this.inChannel = (ReadableByteChannel) in;
    }
}
