class BackupThread extends Thread {
    public void updateCanvasses() {
        int oldN = channelCanvasses.size();
        if (displayMode == ChannelControl.TILED) {
            for (int i = 0; i < oldN; i++) {
                remove((Image5DCanvas) channelCanvasses.get(i));
            }
        }
        channelCanvasses = new Vector();
        for (int i = 1; i <= i5d.getNChannels(); i++) {
            channelCanvasses.add(new Image5DCanvas(i5d.getChannelImagePlus(i)));
            i5d.getChannelImagePlus(i).setWindow(this);
        }
        if (displayMode == ChannelControl.TILED) {
            for (int i = 0; i < i5d.getNChannels(); i++) {
                add((Image5DCanvas) channelCanvasses.get(i), Image5DLayout.CANVAS);
            }
        }
    }
}
