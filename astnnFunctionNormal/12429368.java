class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == individualY) {
                Debug.println(1, "plugin " + getName() + " [individual y] clicked");
                AClipPlotter.setAutoScaleIndividualYEnabled(individualY.isSelected());
            } else if (e.getSource() == autoScale) {
                Debug.println(1, "plugin " + getName() + " [autoscale] clicked");
                AClip c = getFocussedClip();
                AClipPlotter cp = c.getPlotter();
                ALayer l = c.getSelectedLayer();
                AChannel ch = l.getChannel(0);
                AChannelPlotter chp = ch.getPlotter();
                if (autoX.isSelected()) {
                    double offset, length;
                    switch(xAutoMode.getSelectedIndex()) {
                        case 0:
                            cp.autoScaleX();
                            break;
                        case 1:
                            offset = c.getAudio().getLoopStartPointer();
                            length = c.getAudio().getLoopEndPointer() - c.getAudio().getLoopStartPointer();
                            cp.setXRange(offset - .03f * length, length * 1.06f);
                            break;
                        case 2:
                            offset = GPMeasure.getLowerCursor();
                            length = GPMeasure.getHigherCursor() - GPMeasure.getLowerCursor();
                            cp.setXRange(offset - .03f * length, length * 1.06f);
                            break;
                        case 3:
                            offset = l.getSelection().getLowestSelectedIndex();
                            length = l.getSelection().getHighestSelectedIndex() - l.getSelection().getLowestSelectedIndex();
                            cp.setXRange(offset - .03f * length, length * 1.06f);
                            break;
                    }
                }
                if (autoY.isSelected()) {
                    int xOffset, xLength;
                    float maxValue;
                    switch(yAutoMode.getSelectedIndex()) {
                        case 0:
                            cp.autoScaleY();
                            break;
                        case 1:
                            xOffset = (int) chp.getXOffset();
                            xLength = (int) chp.getXLength();
                            cp.autoScaleY(xOffset, xLength);
                            break;
                        case 2:
                            maxValue = 1 << (c.getSampleWidth() - 1);
                            cp.setYRange(-maxValue * 1.03f, 2 * maxValue * 1.03f);
                            break;
                        case 3:
                            xOffset = l.getSelection().getLowestSelectedIndex();
                            xLength = l.getSelection().getHighestSelectedIndex() - l.getSelection().getLowestSelectedIndex();
                            cp.autoScaleY(xOffset, xLength);
                            break;
                    }
                }
                reloadZoomFrame();
                reloadFocussedClipEditor();
            }
        }
}
