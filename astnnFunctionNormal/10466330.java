class BackupThread extends Thread {
    public void mergeDownChannel(int index) {
        if ((getNumberOfElements() > 1) && (index > 0)) {
            AChannelSelection ch1 = getChannel(index - 1).createSelection();
            AChannelSelection ch2 = getChannel(index).createSelection();
            ALayerSelection l = new ALayerSelection(this);
            l.addChannelSelection(ch1);
            l.addChannelSelection(ch2);
            l.operateChannel0WithChannel1(new AOMix());
            remove(index);
        }
    }
}
