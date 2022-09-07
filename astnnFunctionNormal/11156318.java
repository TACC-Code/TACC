class BackupThread extends Thread {
    public double getAutoscaleXOffset() {
        double min = Double.MAX_VALUE;
        double f;
        ALayer l = getLayerModel();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            f = l.getChannel(i).getPlotter().getAutoscaleXOffset();
            if (f < min) {
                min = f;
            }
        }
        return min;
    }
}
