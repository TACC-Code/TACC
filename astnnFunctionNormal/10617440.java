class BackupThread extends Thread {
    public void paintXSkala(Graphics2D g2d, Rectangle rect, Color color) {
        if (getXLength() < .1) return;
        g2d.setClip(rect.x, rect.y - 200, rect.width, rect.height + 200);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
        AChannel ch = getChannelModel();
        int rangeBegin = Math.max(0, sampleToGraphX(0));
        int rangeEnd = Math.min(rect.x + rect.width, sampleToGraphX(ch.getSampleLength()));
        g2d.setColor(Color.white);
        g2d.fillRect(rangeBegin, rect.y, rangeEnd - rangeBegin, rect.height);
        AClipPlotter cp = ((AClip) model.getParent().getParent()).getPlotter();
        double oldU = cp.toPlotterXUnit(graphToSampleX(rect.x));
        int oldL = -1000;
        int deltaL = 20;
        int deltaP = 7;
        int minorLineLength = (int) (rect.height * 0.3);
        int mediumLineLength = (int) (rect.height * 0.5);
        int majorLineLength = (int) (rect.height * 0.8);
        for (int i = 0; i < rect.width; i++) {
            double u = cp.toPlotterXUnit(graphToSampleX(rect.x + i));
            double deltaU = u - cp.toPlotterXUnit(graphToSampleX(rect.x + i + deltaP));
            double decadedU = Math.pow(10, Math.round(Math.log10(Math.abs(deltaU))));
            if (((int) (u / decadedU / 10)) != ((int) (oldU / decadedU / 10)) || Math.signum(u) != Math.signum(oldU)) {
                g2d.setColor(Color.gray);
                g2d.drawLine(rect.x + i, rect.y, rect.x + i, rect.y + majorLineLength);
                if (AClipPlotter.isSkalaValuesVisible()) {
                    oldL = i;
                    double printedU = Math.round(u / decadedU) * decadedU;
                    g2d.setColor(color);
                    paintText(g2d, doubleToString(printedU) + cp.getPlotterXUnitName(), 10, rect.x + i, rect.y - 6, true);
                }
            } else if (((int) (u / decadedU / 5)) != ((int) (oldU / decadedU / 5)) || Math.signum(u) != Math.signum(oldU)) {
                g2d.setColor(Color.gray);
                g2d.drawLine(rect.x + i, rect.y, rect.x + i, rect.y + mediumLineLength);
                if (i > (oldL + deltaL) && AClipPlotter.isSkalaValuesVisible()) {
                    double printedU = Math.round(u / decadedU) * decadedU;
                    g2d.setColor(color);
                    paintText(g2d, doubleToString(printedU) + cp.getPlotterXUnitName(), 10, rect.x + i, rect.y - 6, true);
                }
            } else if (((int) (u / decadedU)) != ((int) (oldU / decadedU)) || Math.signum(u) != Math.signum(oldU)) {
                g2d.setColor(Color.gray);
                g2d.drawLine(rect.x + i, rect.y, rect.x + i, rect.y + minorLineLength);
            }
            oldU = u;
        }
    }
}
