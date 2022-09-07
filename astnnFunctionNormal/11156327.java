class BackupThread extends Thread {
    public void paintAllSelections(Graphics2D g2d, Rectangle layerRect, Color surfaceColor, Color lineColor, int maxSampleLength, int numberOfChannels) {
        if (isVisible) {
            ALayer l = getLayerModel();
            for (int i = 0; i < numberOfChannels; i++) {
                Rectangle subRect = createChannelRect(layerRect, i, numberOfChannels);
                if (i < l.getNumberOfChannels()) {
                    l.getChannel(i).getPlotter().setRectangle(subRect);
                    l.getChannel(i).getPlotter().paintSelection(g2d, surfaceColor, lineColor);
                }
            }
        }
    }
}
