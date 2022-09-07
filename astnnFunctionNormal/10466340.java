class BackupThread extends Thread {
    public void setSelection(ALayerSelection s) {
        int n = Math.min(getNumberOfChannels(), s.getNumberOfChannelSelections());
        s.setLayer(this);
        for (int i = 0; i < n; i++) {
            getChannel(i).setSelection(s.getChannelSelection(i));
            s.getChannelSelection(i).setChannel(getChannel(i));
        }
        selection = s;
    }
}
