class BackupThread extends Thread {
    private String makeDescription(AudioFileFormat f) {
        StringBuffer buf = new StringBuffer();
        AudioFileFormat.Type type = f.getType();
        int byteLength = f.getByteLength();
        int frameLength = f.getFrameLength();
        AudioFormat fmt = f.getFormat();
        if (type != null) {
            buf.append(ZUtilities.makeExactLengthString("File type:", DESC_FIELD_LEN) + type.toString() + " (." + type.getExtension() + ")");
        } else {
            buf.append(ZUtilities.makeExactLengthString("File type:", DESC_FIELD_LEN) + "Unknown");
        }
        buf.append(Zoeos.lineSeperator);
        float sr = fmt.getSampleRate();
        if (sr - (int) sr == 0) buf.append(ZUtilities.makeExactLengthString("Sample rate:", DESC_FIELD_LEN) + (int) sr + " Hz"); else buf.append(ZUtilities.makeExactLengthString("Sample rate:", DESC_FIELD_LEN) + sr + " Hz");
        buf.append(Zoeos.lineSeperator);
        buf.append(ZUtilities.makeExactLengthString("Sample size:", DESC_FIELD_LEN) + fmt.getSampleSizeInBits() + " bit");
        buf.append(Zoeos.lineSeperator);
        if (byteLength != AudioSystem.NOT_SPECIFIED) {
            if (byteLength < 1024) buf.append(ZUtilities.makeExactLengthString("Size:", DESC_FIELD_LEN) + byteLength + " bytes"); else if (byteLength < 1024 * 1024) buf.append(ZUtilities.makeExactLengthString("Size:", DESC_FIELD_LEN) + df.format(byteLength / 1024.0) + " KB"); else buf.append(ZUtilities.makeExactLengthString("Size:", DESC_FIELD_LEN) + df.format(byteLength / (1024.0 * 1024.0)) + " MB");
            buf.append(Zoeos.lineSeperator);
        }
        if (frameLength != AudioSystem.NOT_SPECIFIED) {
            buf.append(ZUtilities.makeExactLengthString("Length:", DESC_FIELD_LEN) + frameLength + " samples");
            buf.append(Zoeos.lineSeperator);
        }
        int chnls = fmt.getChannels();
        if (chnls == 1) buf.append(ZUtilities.makeExactLengthString("Channels:", DESC_FIELD_LEN) + "Mono"); else if (chnls == 2) buf.append(ZUtilities.makeExactLengthString("Channels:", DESC_FIELD_LEN) + "Stereo"); else buf.append(ZUtilities.makeExactLengthString("Channels:", DESC_FIELD_LEN) + chnls);
        buf.append(Zoeos.lineSeperator);
        buf.append(ZUtilities.makeExactLengthString("Encoding:", DESC_FIELD_LEN) + fmt.getEncoding());
        buf.append(Zoeos.lineSeperator);
        return new String(buf);
    }
}
