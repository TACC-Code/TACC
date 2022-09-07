class BackupThread extends Thread {
    public double getAutoscaleXLength() {
        double max = Double.MIN_VALUE;
        double f;
        ALayer l = getLayerModel();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            f = l.getChannel(i).getPlotter().getAutoscaleXLength();
            if (f > max) {
                max = f;
            }
        }
        return max;
    }
}
