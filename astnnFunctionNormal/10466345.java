class BackupThread extends Thread {
    public void setFullSelection() {
        ALayerSelection s = new ALayerSelection(this);
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).setFullSelection();
            s.addChannelSelection(getChannel(i).getSelection());
        }
        selection = s;
    }
}
