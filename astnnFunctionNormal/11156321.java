class BackupThread extends Thread {
    public float getAutoscaleYLength(int xOffset, int xLength) {
        float max = Float.MIN_VALUE;
        float f;
        ALayer l = getLayerModel();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            f = l.getChannel(i).getPlotter().getAutoscaleYLength(xOffset, xLength);
            if (f > max) {
                max = f;
            }
        }
        return max;
    }
}
