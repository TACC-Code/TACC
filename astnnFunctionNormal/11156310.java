class BackupThread extends Thread {
    public void translateYOffset(float offset) {
        for (int i = 0; i < getLayerModel().getNumberOfElements(); i++) {
            getLayerModel().getChannel(i).getPlotter().translateYOffset(offset);
        }
    }
}
