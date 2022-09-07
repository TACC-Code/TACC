class BackupThread extends Thread {
    public void zoomY(double factor) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().zoomY(factor);
        }
    }
}
