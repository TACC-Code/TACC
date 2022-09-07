class BackupThread extends Thread {
    public ALayerSelection getSelection() {
        ALayerSelection s = new ALayerSelection(this);
        for (int i = 0; i < getNumberOfChannels(); i++) {
            AChannelSelection chS = getChannel(i).getSelection();
            if (chS.isSelected()) {
                s.addChannelSelection(chS);
            }
        }
        if (!s.isSelected()) {
            return createSelection();
        }
        return s;
    }
}
