class BackupThread extends Thread {
    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
        AChannelPlotter chpCtrl = getFocussedClip().getSelectedLayer().getChannel(cookie.ctrlChannelIndex).getPlotter();
        int graphCtrlX = chpCtrl.sampleToGraphX(cookie.ctrlX);
        int graphCtrlY = chpCtrl.sampleToGraphY((float) cookie.ctrlY);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.white);
        g2d.drawLine(0, graphCtrlY, (int) rect.getWidth(), graphCtrlY);
        g2d.drawLine(graphCtrlX, 0, graphCtrlX, (int) rect.getHeight());
        g2d.setStroke(ctrlStroke);
        g2d.setColor(Color.black);
        g2d.drawLine(0, graphCtrlY, (int) rect.getWidth(), graphCtrlY);
        g2d.drawLine(graphCtrlX, 0, graphCtrlX, (int) rect.getHeight());
        if (cursorMode.getSelectedIndex() == 2) {
            g2d.setStroke(fatStroke);
            g2d.setColor(Color.red);
            if (ctrlPositivePeak) {
                g2d.drawLine(graphCtrlX, graphCtrlY, graphCtrlX - 4, graphCtrlY - 4);
                g2d.drawLine(graphCtrlX, graphCtrlY, graphCtrlX + 4, graphCtrlY - 4);
            } else {
                g2d.drawLine(graphCtrlX, graphCtrlY, graphCtrlX - 4, graphCtrlY + 4);
                g2d.drawLine(graphCtrlX, graphCtrlY, graphCtrlX + 4, graphCtrlY + 4);
            }
        }
        AChannelPlotter chpShift = getFocussedClip().getSelectedLayer().getChannel(cookie.shiftChannelIndex).getPlotter();
        int graphShiftX = chpShift.sampleToGraphX(cookie.shiftX);
        int graphShiftY = chpShift.sampleToGraphY((float) cookie.shiftY);
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.white);
        g2d.drawLine(0, graphShiftY, (int) rect.getWidth(), graphShiftY);
        g2d.drawLine(graphShiftX, 0, graphShiftX, (int) rect.getHeight());
        g2d.setStroke(shiftStroke);
        g2d.setColor(Color.black);
        g2d.drawLine(0, graphShiftY, (int) rect.getWidth(), graphShiftY);
        g2d.drawLine(graphShiftX, 0, graphShiftX, (int) rect.getHeight());
        if (cursorMode.getSelectedIndex() == 2) {
            g2d.setStroke(fatStroke);
            g2d.setColor(Color.red);
            if (shiftPositivePeak) {
                g2d.drawLine(graphShiftX, graphShiftY, graphShiftX - 4, graphShiftY - 4);
                g2d.drawLine(graphShiftX, graphShiftY, graphShiftX + 4, graphShiftY - 4);
            } else {
                g2d.drawLine(graphShiftX, graphShiftY, graphShiftX - 4, graphShiftY + 4);
                g2d.drawLine(graphShiftX, graphShiftY, graphShiftX + 4, graphShiftY + 4);
            }
        }
    }
}
