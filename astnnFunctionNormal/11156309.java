class BackupThread extends Thread {
    public void translateXOffset(double offset) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().translateXOffset(offset);
        }
    }
}
