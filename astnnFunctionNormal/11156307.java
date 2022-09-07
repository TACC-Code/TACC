class BackupThread extends Thread {
    public void setXRange(double offset, double length) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().setXRange(offset, length);
        }
    }
}
