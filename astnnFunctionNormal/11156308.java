class BackupThread extends Thread {
    public void setYRange(float offset, float length) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().setYRange(offset, length);
        }
    }
}
