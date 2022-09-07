class BackupThread extends Thread {
    public JavaSoundAudioLine(AudioFormat format, Mixer.Info info, String label) {
        this.format = format;
        mixerInfo = info;
        this.label = label;
        switch(format.getChannels()) {
            case 1:
                channelFormat = ChannelFormat.MONO;
            case 2:
                channelFormat = ChannelFormat.STEREO;
        }
    }
}
