class BackupThread extends Thread {
    public int getInsideChannelIndex(Point p) {
        try {
            ALayer l = getLayerModel();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                if (l.getChannel(i).getPlotter().isInsideChannel(p)) return i;
            }
        } catch (NullPointerException npe) {
        }
        return -1;
    }
}
