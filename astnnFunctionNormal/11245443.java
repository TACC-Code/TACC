class BackupThread extends Thread {
    private void calculateCursorPositions(MouseEvent e) {
        if (frame.isVisible() && (getFocussedClip() != null)) {
            ALayer l = getFocussedClip().getSelectedLayer();
            int i = l.getPlotter().getInsideChannelIndex(e.getPoint());
            if (i >= 0) {
                AChannel ch = l.getChannel(i);
                AChannelPlotter cp = ch.getPlotter();
                double x = cp.graphToSampleX(e.getPoint().x);
                double y = cp.graphToSampleY(e.getPoint().y);
                boolean positivePeak = true;
                switch(cursorMode.getSelectedIndex()) {
                    case 1:
                        x = l.getChannel(i).limitIndex((int) x);
                        y = l.getChannel(i).getSample((int) x);
                        break;
                    case 2:
                        int xPeakRange = (int) (cp.graphToSampleX(e.getPoint().x + 15) - x);
                        int peakIndex;
                        y = l.getChannel(i).getSample((int) x);
                        if (cp.graphToSampleY(e.getPoint().y) > y) {
                            peakIndex = AOToolkit.getNearestPositivePeakIndex(ch.getSamples(), (int) x, xPeakRange);
                            positivePeak = true;
                        } else {
                            peakIndex = AOToolkit.getNearestNegativePeakIndex(ch.getSamples(), (int) x, xPeakRange);
                            positivePeak = false;
                        }
                        x = l.getChannel(i).limitIndex(peakIndex);
                        y = l.getChannel(i).getSample((int) x);
                        break;
                }
                if (GToolkit.isShiftKey(e)) {
                    shiftXSample = x;
                    cookie.shiftX = x;
                    cookie.shiftY = y;
                    shiftPositivePeak = positivePeak;
                    cookie.shiftChannelIndex = i;
                }
                if (GToolkit.isCtrlKey(e)) {
                    ctrlXSample = x;
                    cookie.ctrlX = x;
                    cookie.ctrlY = y;
                    ctrlPositivePeak = positivePeak;
                    cookie.ctrlChannelIndex = i;
                }
                repaintFocussedClipEditor();
            }
        }
    }
}
