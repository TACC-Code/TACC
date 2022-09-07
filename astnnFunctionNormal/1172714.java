class BackupThread extends Thread {
    private void dumpAudioFileFormat(AudioFileFormat baseFileFormat, PrintStream out, String info) throws UnsupportedAudioFileException {
        if (out != null) {
            out.println("  -----  " + info + "  -----");
            out.println("    ByteLength=" + baseFileFormat.getByteLength());
            out.println("    getFrameLength=" + baseFileFormat.getFrameLength());
            out.println("    getType=" + baseFileFormat.getType());
            AudioFormat baseFormat = baseFileFormat.getFormat();
            out.println("    Source Format=" + baseFormat.toString());
            out.println("    Channels=" + baseFormat.getChannels());
            out.println("    FrameRate=" + baseFormat.getFrameRate());
            out.println("    FrameSize=" + baseFormat.getFrameSize());
            out.println("    SampleRate=" + baseFormat.getSampleRate());
            out.println("    SampleSizeInBits=" + baseFormat.getSampleSizeInBits());
            out.println("    Encoding=" + baseFormat.getEncoding());
        }
    }
}
