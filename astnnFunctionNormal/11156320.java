class BackupThread extends Thread {
    public float getAutoscaleYOffset(int xOffset, int xLength) {
        float min = Float.MAX_VALUE;
        float f;
        ALayer l = getLayerModel();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            f = l.getChannel(i).getPlotter().getAutoscaleYOffset(xOffset, xLength);
            if (f < min) {
                min = f;
            }
        }
        return min;
    }
}
