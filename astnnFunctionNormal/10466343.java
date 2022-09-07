class BackupThread extends Thread {
    public ALayerSelection createSelection() {
        ALayerSelection s = new ALayerSelection(this);
        for (int i = 0; i < getNumberOfChannels(); i++) {
            s.addChannelSelection(getChannel(i).createSelection());
        }
        return s;
    }
}
