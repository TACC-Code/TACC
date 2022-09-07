class BackupThread extends Thread {
    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int count = src.remaining();
        final long remaining = this.remaining;
        if (count > 0) {
            if (count <= remaining) {
                int written = 0;
                try {
                    final OutputStream out = ensureOpen();
                    final WritableByteChannel outChannel;
                    if (out instanceof WritableByteChannel) outChannel = (WritableByteChannel) out; else if (out instanceof FileOutputStream) outChannel = ((FileOutputStream) out).getChannel(); else outChannel = null;
                    if (outChannel == null) {
                        if (src.hasArray()) {
                            out.write(src.array(), src.arrayOffset() + src.position(), count);
                            src.position(src.limit());
                            written = count;
                        } else {
                            while (written < count) {
                                out.write(src.get());
                                written++;
                            }
                        }
                    } else {
                        while (written < count) {
                            int step = outChannel.write(src);
                            if (step >= 0) {
                                if (step <= count - written) {
                                    if (step != 0) {
                                        written += count;
                                        continue;
                                    } else {
                                        this.remaining = remaining - written;
                                        written = 0;
                                        super.write(src);
                                        return count;
                                    }
                                } else {
                                    throw new ReturnValueException(outChannel, "write(ByteBuffer)", step, "<= ", count - written);
                                }
                            } else {
                                throw new ReturnValueException(outChannel, "write(ByteBuffer)", step, ">= 0");
                            }
                        }
                    }
                } finally {
                    this.remaining = remaining - written;
                }
            } else {
                throw new IOException("Can not write " + count + " bytes, only " + remaining + " remaining.");
            }
        }
        if (remaining == count) eof();
        return count;
    }
}
