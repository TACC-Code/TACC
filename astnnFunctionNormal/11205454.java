class BackupThread extends Thread {
    private void volumeSliderStateChanged(ChangeEvent evt) {
        if (getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitVolumeChange(this, getChannelNumber(), volumeSlider.getValue());
    }
}
