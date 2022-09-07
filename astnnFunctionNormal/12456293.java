class BackupThread extends Thread {
    public static void createBusStrips(MixerControls mixerControls, String mainStripName, ChannelFormat mainFormat, int nreturns) {
        mixerControls.createStripControls(MAIN_STRIP, 0, mainStripName, mainFormat);
        List<BusControls> busControlsList = mixerControls.getAuxBusControls();
        int naux = busControlsList.size();
        for (int i = 0; i < naux; i++) {
            mixerControls.createStripControls(AUX_STRIP, i, busControlsList.get(i).getName(), false, busControlsList.get(i).getChannelFormat());
        }
        busControlsList = mixerControls.getFxBusControls();
        int nsends = busControlsList.size();
        for (int i = 0; i < nsends; i++) {
            mixerControls.createStripControls(FX_STRIP, i, busControlsList.get(i).getName(), i < nreturns, busControlsList.get(i).getChannelFormat());
        }
    }
}
