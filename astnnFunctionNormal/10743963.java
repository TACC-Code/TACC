class BackupThread extends Thread {
    void savePoints(Roi roi) {
        if (imp == null) {
            rt.addValue("X", 0.0);
            rt.addValue("Y", 0.0);
            rt.addValue("Slice", 0.0);
            return;
        }
        if ((measurements & AREA) != 0) rt.addValue(ResultsTable.AREA, 0);
        Polygon p = roi.getPolygon();
        ImageProcessor ip = imp.getProcessor();
        Calibration cal = imp.getCalibration();
        int x = p.xpoints[0];
        int y = p.ypoints[0];
        double value = ip.getPixelValue(x, y);
        if (markWidth > 0 && !Toolbar.getMultiPointMode()) {
            ip.setColor(Toolbar.getForegroundColor());
            ip.setLineWidth(markWidth);
            ip.moveTo(x, y);
            ip.lineTo(x, y);
            imp.updateAndDraw();
            ip.setLineWidth(Line.getWidth());
        }
        rt.addValue("X", cal.getX(x));
        rt.addValue("Y", cal.getY(y, imp.getHeight()));
        if (imp.isHyperStack() || imp.isComposite()) {
            if (imp.getNChannels() > 1) rt.addValue("Ch", imp.getChannel());
            if (imp.getNSlices() > 1) rt.addValue("Slice", imp.getSlice());
            if (imp.getNFrames() > 1) rt.addValue("Frame", imp.getFrame());
        } else rt.addValue("Slice", cal.getZ(imp.getCurrentSlice()));
        if (imp.getProperty("FHT") != null) {
            double center = imp.getWidth() / 2.0;
            y = imp.getHeight() - y - 1;
            double r = Math.sqrt((x - center) * (x - center) + (y - center) * (y - center));
            if (r < 1.0) r = 1.0;
            double theta = Math.atan2(y - center, x - center);
            theta = theta * 180.0 / Math.PI;
            if (theta < 0) theta = 360.0 + theta;
            rt.addValue("R", (imp.getWidth() / r) * cal.pixelWidth);
            rt.addValue("Theta", theta);
        }
    }
}
