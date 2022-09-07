class BackupThread extends Thread {
    public void mouseWheelMoved(MouseWheelEvent e) {
        ALayer l = getFocussedClip().getSelectedLayer();
        int i = l.getPlotter().getInsideChannelIndex(e.getPoint());
        AChannelPlotter chp = l.getChannel(i).getPlotter();
        double xc = chp.graphToSampleX(e.getX());
        double yc = chp.graphToSampleY(e.getY());
        int m = e.getWheelRotation();
        AClipPlotter cp = getFocussedClip().getPlotter();
        ALayerPlotter lp = getFocussedClip().getSelectedLayer().getPlotter();
        final double factor = 1.1f;
        if (GToolkit.isShiftKey(e)) {
            if (GToolkit.isCtrlKey(e)) {
                cp.zoomX(m > 0 ? factor : 1f / factor, xc);
                if (individualY.isSelected()) {
                    lp.zoomY(m > 0 ? factor : 1f / factor, yc);
                } else {
                    cp.zoomY(m > 0 ? factor : 1f / factor, yc);
                }
            } else {
                cp.zoomX(m > 0 ? factor : 1f / factor, xc);
            }
        } else {
            if (GToolkit.isCtrlKey(e)) {
                if (individualY.isSelected()) {
                    lp.zoomY(m > 0 ? factor : 1f / factor, yc);
                } else {
                    cp.zoomY(m > 0 ? factor : 1f / factor, yc);
                }
            } else {
                final double shiftFactor = 0.035f;
                if (individualY.isSelected()) {
                    cp.translateXOffset(((AChannelPlotter) lp.getLayerModel().getChannel(0).getPlotter()).getXLength() * (m > 0 ? shiftFactor : -shiftFactor));
                } else {
                    cp.translateXOffset(((AChannelPlotter) cp.getClipModel().getLayer(0).getChannel(0).getPlotter()).getXLength() * (m > 0 ? shiftFactor : -shiftFactor));
                }
            }
        }
        reloadZoomFrame();
        pluginHandler.getFocussedClipEditor().reload();
    }
}
