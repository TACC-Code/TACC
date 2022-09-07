class BackupThread extends Thread {
    public void paintMarker(Graphics2D g2d, Rectangle channelRect, Rectangle scalaRect) {
        getChannelModel().getMarker().paintOntoClip(g2d, channelRect, scalaRect);
    }
}
