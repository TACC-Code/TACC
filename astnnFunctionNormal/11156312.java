class BackupThread extends Thread {
    public void zoomX(double factor, double xCenter) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().zoomX(factor, xCenter);
        }
    }
}
