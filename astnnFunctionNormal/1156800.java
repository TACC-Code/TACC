class BackupThread extends Thread {
    public AudioMixerBus(AudioMixer mixer, BusControls busControls) {
        name = busControls.getName();
        isFx = busControls.getId() == FX_BUS;
        channelFormat = busControls.getChannelFormat();
        buffer = mixer.createBuffer(name);
        buffer.setChannelFormat(channelFormat);
        setMeterProcess(new MeterProcess(busControls.getMeterControls()));
    }
}
