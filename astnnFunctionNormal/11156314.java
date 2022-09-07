class BackupThread extends Thread {
    public void zoomY(double factor, double yCenter) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().zoomY(factor, yCenter);
        }
    }
}
