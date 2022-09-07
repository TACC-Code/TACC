class BackupThread extends Thread {
    public void setInstrument(int instrument) {
        synth.getChannels()[0].programChange(instrument);
    }
}
