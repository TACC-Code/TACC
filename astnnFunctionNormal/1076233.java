class BackupThread extends Thread {
    public void write(AudioInputStream stream, RIFFWriter writer) throws IOException {
        RIFFWriter fmt_chunk = writer.writeChunk("fmt ");
        AudioFormat format = stream.getFormat();
        fmt_chunk.writeUnsignedShort(3);
        fmt_chunk.writeUnsignedShort(format.getChannels());
        fmt_chunk.writeUnsignedInt((int) format.getSampleRate());
        fmt_chunk.writeUnsignedInt(((int) format.getFrameRate()) * format.getFrameSize());
        fmt_chunk.writeUnsignedShort(format.getFrameSize());
        fmt_chunk.writeUnsignedShort(format.getSampleSizeInBits());
        fmt_chunk.close();
        RIFFWriter data_chunk = writer.writeChunk("data");
        byte[] buff = new byte[1024];
        int len;
        while ((len = stream.read(buff, 0, buff.length)) != -1) data_chunk.write(buff, 0, len);
        data_chunk.close();
    }
}
