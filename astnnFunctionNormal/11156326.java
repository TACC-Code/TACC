class BackupThread extends Thread {
    public void paintLayer(Graphics2D g2d, Rectangle layerRect, int maxSampleLength, int maxNumberOfChannels, boolean skalaEnable, boolean volumeSensitive, Color bgColor, float alpha, boolean paintBackground, Color gridColor) {
        if (isVisible || !volumeSensitive) {
            ALayer l = getLayerModel();
            for (int i = 0; i < maxNumberOfChannels; i++) {
                Rectangle channelRect = createChannelRect(layerRect, i, maxNumberOfChannels);
                if (i < l.getNumberOfChannels()) {
                    l.getChannel(i).getPlotter().setRectangle(channelRect);
                    if (i < l.getNumberOfChannels()) {
                        if (paintBackground) {
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
                            l.getChannel(i).getPlotter().paintBackground(g2d, bgColor);
                        }
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                        l.getChannel(i).getPlotter().paintSamples(g2d, color, colorGamma);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
                        l.getChannel(i).getPlotter().paintFrame(g2d);
                    }
                    if (skalaEnable && (i < l.getNumberOfChannels())) {
                        Rectangle xr = createXSkalaRect(layerRect, i, maxNumberOfChannels);
                        Rectangle yr = createYSkalaRect(layerRect, i, maxNumberOfChannels);
                        l.getChannel(i).getPlotter().paintXSkala(g2d, xr, gridColor);
                        l.getChannel(i).getPlotter().paintYSkala(g2d, yr, gridColor);
                        if (AClipPlotter.isGridVisible()) {
                            l.getChannel(i).getPlotter().paintGrid(g2d, channelRect, gridColor);
                        }
                    }
                }
            }
        }
    }
}
