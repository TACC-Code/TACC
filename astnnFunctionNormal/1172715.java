class BackupThread extends Thread {
    private void dumpAudioInputStream(AudioInputStream in, PrintStream out, String info) throws IOException {
        if (out != null) {
            out.println("  -----  " + info + "  -----");
            out.println("    Available=" + in.available());
            out.println("    FrameLength=" + in.getFrameLength());
            AudioFormat baseFormat = in.getFormat();
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
