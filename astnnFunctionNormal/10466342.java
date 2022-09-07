class BackupThread extends Thread {
    public ALayer2DSelection get2DSelection() {
        ALayer2DSelection s = new ALayer2DSelection(this);
        for (int i = 0; i < getNumberOfChannels(); i++) {
            AChannel2DSelection chS = getChannel(i).get2DSelection();
            if (chS.isSelected()) {
                s.addChannelSelection(chS);
            }
        }
        return s;
    }
}
