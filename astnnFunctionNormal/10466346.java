class BackupThread extends Thread {
    public void setEmptySelection() {
        ALayerSelection s = new ALayerSelection(this);
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).setEmptySelection();
            s.addChannelSelection(getChannel(i).getSelection());
        }
        selection = s;
    }
}
