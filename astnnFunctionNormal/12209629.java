class BackupThread extends Thread {
    public void updateFormat() {
        if (!track.isEnabled()) return;
        ProgressControl pc;
        if ((pc = progressControl()) == null) return;
        StringControl sc;
        if (track.getFormat() instanceof AudioFormat) {
            String channel = "";
            AudioFormat afmt = (AudioFormat) track.getFormat();
            sc = pc.getAudioCodec();
            sc.setValue(afmt.getEncoding());
            sc = pc.getAudioProperties();
            if (afmt.getChannels() == 1) channel = "mono"; else channel = "stereo";
            sc.setValue(afmt.getSampleRate() / 1000.0 + " KHz, " + afmt.getSampleSizeInBits() + "-bit, " + channel);
        }
        if (track.getFormat() instanceof VideoFormat) {
            VideoFormat vfmt = (VideoFormat) track.getFormat();
            sc = pc.getVideoCodec();
            sc.setValue(vfmt.getEncoding());
            sc = pc.getVideoProperties();
            if (vfmt.getSize() != null) sc.setValue(vfmt.getSize().width + " X " + vfmt.getSize().height);
        }
    }
}
