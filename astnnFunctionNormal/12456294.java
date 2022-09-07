class BackupThread extends Thread {
    public static void createGroupStrips(MixerControls mixerControls, int ngroups) {
        ChannelFormat mainFormat = mixerControls.getMainBusControls().getChannelFormat();
        for (int i = 0; i < ngroups; i++) {
            mixerControls.createStripControls(GROUP_STRIP, i, String.valueOf((char) ('A' + i)), mainFormat);
        }
    }
}
