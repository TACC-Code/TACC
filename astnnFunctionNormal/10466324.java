class BackupThread extends Thread {
    public void setPlotType(int type) {
        plotType = type;
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).setPlotType(type);
        }
    }
}
