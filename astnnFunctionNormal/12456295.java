class BackupThread extends Thread {
    public static void createChannelStrips(MixerControls mixerControls, int nchannels) {
        ChannelFormat mainFormat = mixerControls.getMainBusControls().getChannelFormat();
        for (int i = 0; i < nchannels; i++) {
            mixerControls.createStripControls(CHANNEL_STRIP, i, String.valueOf(1 + i), mainFormat);
        }
    }
}
