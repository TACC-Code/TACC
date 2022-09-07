class BackupThread extends Thread {
    public void paintMarkers(Graphics2D g2d, Rectangle layerRect, int maxSampleLength, int numberOfChannels) {
        if (isVisible) {
            ALayer l = getLayerModel();
            for (int i = 0; i < numberOfChannels; i++) {
                if (i < l.getNumberOfChannels()) {
                    Rectangle chr = createChannelRect(layerRect, i, numberOfChannels);
                    Rectangle xr = createXSkalaRect(layerRect, i, numberOfChannels);
                    l.getChannel(i).getPlotter().paintMarker(g2d, chr, xr);
                }
            }
        }
    }
}
