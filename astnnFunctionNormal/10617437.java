class BackupThread extends Thread {
    public void paintSelection(Graphics2D g2d, Color surfaceColor, Color lineColor) {
        AChannelSelection s = getChannelModel().getSelection();
        AClipPlotter cp = ((AClip) model.getParent().getParent()).getPlotter();
        if (s.isSelected()) {
            int xLeft = sampleToGraphX(s.getOffset());
            int y = rectangle.y + 2;
            int xRight = sampleToGraphX(s.getOffset() + s.getLength());
            int h = rectangle.height - 5;
            if (xLeft < 0) xLeft = -50; else if (xLeft > rectangle.width) return;
            if (xRight > rectangle.width) xRight = rectangle.width + 50; else if (xRight < 0) return;
            g2d.setColor(surfaceColor);
            g2d.setClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .18f));
            g2d.fillRect(xLeft, y, xRight - xLeft, h);
            float dash[] = { 4.f, 4.f };
            g2d.setStroke(new BasicStroke(1.f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.f, dash, 0.f));
            g2d.setColor(lineColor);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
            g2d.drawRect(xLeft, y, xRight - xLeft, h);
            g2d.setStroke(new BasicStroke());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
            int oldPx = 0;
            int oldPy = 0;
            for (int i = xLeft; i < xRight; i += 2) {
                int px = i;
                float intY = s.getIntensity((int) graphToSampleX(i));
                int py = (int) ((1 - intY) * h) + y;
                g2d.setColor(lineColor);
                if (i != xLeft) {
                    g2d.drawLine(oldPx, oldPy, px, py);
                }
                oldPx = px;
                oldPy = py;
            }
            for (int i = 0; i < s.getIntensityPoints().size(); i++) {
                AChannelSelection.Point sp = (AChannelSelection.Point) s.getIntensityPoints().get(i);
                int px = sampleToGraphX(s.getOffset() + (s.getLength() * sp.x));
                int py = (int) ((1 - sp.y) * h) + y;
                if (s.getActiveIntensityPointIndex() == i) {
                    g2d.setColor(Color.red);
                    g2d.drawRect(px - 4, py - 4, 8, 8);
                } else {
                    g2d.setColor(lineColor);
                }
                g2d.fillRect(px - 2, py - 2, 5, 5);
                float yy = (float) sp.y;
                String ss = "" + yy;
                ss = ss.substring(0, Math.min(ss.length(), 5));
                g2d.setColor(lineColor);
                AChannelPlotter.paintText(g2d, ss, 10, px, yy > 0.5 ? py + 10 : py - 10, false);
            }
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            nf.setGroupingUsed(false);
            g2d.setColor(lineColor);
            g2d.setFont(new Font("Courrier", Font.PLAIN, 12));
            FontMetrics fm = g2d.getFontMetrics();
            String topStr = "" + nf.format(cp.toPlotterXUnit(s.getOffset())) + cp.getPlotterXUnitName();
            String bottomStr = "" + nf.format(cp.toPlotterXUnit(s.getLength())) + cp.getPlotterXUnitName();
            int mx = (xRight + xLeft) / 2;
            int my = y + (h / 2);
            g2d.drawString(topStr, mx - fm.stringWidth(topStr) / 2, my - fm.getHeight() / 2);
            g2d.drawString(bottomStr, mx - fm.stringWidth(bottomStr) / 2, my + fm.getHeight() / 2);
        }
    }
}
