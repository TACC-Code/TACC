class BackupThread extends Thread {
    public void paintMasks(Graphics2D g2d, Rectangle layerRect, int maxSampleLength, int numberOfChannels) {
        if (isVisible) {
            ALayer l = getLayerModel();
            for (int i = 0; i < numberOfChannels; i++) {
                Rectangle subRect = createChannelRect(layerRect, i, numberOfChannels);
                if (i < l.getNumberOfChannels()) {
                    l.getChannel(i).getPlotter().setRectangle(subRect);
                    l.getChannel(i).getPlotter().paintMask(g2d);
                }
            }
        }
    }
}
