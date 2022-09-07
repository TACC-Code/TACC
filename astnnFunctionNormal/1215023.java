class BackupThread extends Thread {
    private void DrawLines() {
        int ResX = Chart[0].getWidth();
        int ResY = Chart[0].getHeight();
        int MiddleY = ResY / 2;
        double dx, dy;
        int MaxSize;
        int MaximumIncrement;
        if (ConverterSettings.isRun()) {
            MaximumIncrement = (int) (ConverterSettings.getSampleRate() * ConverterSettings.getDisplayTime());
            for (int i = 0; i < CHANNEL_NUMBER; i++) {
                if (Data.getNewData(i) || Resized) {
                    Resized = false;
                    if (ConverterSettings.getTriggerMode() == LCPDConverterSettings.LCPDTriggerMode.Normal || ConverterSettings.getTriggerMode() == LCPDConverterSettings.LCPDTriggerMode.SingleSlope) {
                        if (Data.getNewData(i)) {
                            locData[i].clear();
                            locData[i].addAll(Data.getSamples(i));
                        }
                        ChartLine[i].setBackground(LineBackgroundColor);
                        ChartLine[i].clearRect(0, 0, ResX, ResY);
                        PixelCounter[i] = 0;
                        currentP[i] = 0;
                    } else {
                        if (Data.getNewData(i)) {
                            MaxSize = (int) (ConverterSettings.getDisplayTime() * ConverterSettings.getSampleRate());
                            tempSampleBuffer.clear();
                            tempSampleBuffer.addAll(Data.getSamples(i));
                            for (int t = 0; t < tempSampleBuffer.size(); t++, currentP[i]++) {
                                if (currentP[i] >= MaxSize) {
                                    currentP[i] = 0;
                                }
                                if (currentP[i] < locData[i].size()) locData[i].set(currentP[i], (float) tempSampleBuffer.get(t)); else locData[i].add((float) tempSampleBuffer.get(t));
                            }
                        }
                        ChartLine[i].setBackground(LineBackgroundColor);
                        ChartLine[i].clearRect(0, 0, ResX, ResY);
                        PixelCounter[i] = 0;
                    }
                    if ((ConverterSettings.getSampleRate() != 0) && (ConverterSettings.getMaxVoltage(i) != 0) && GraphicSettings.getDisplayChannel(i) && locData[i].size() > 0) {
                        dx = (double) ResX / (MaximumIncrement - 2);
                        dy = (double) ResY / (2 * ConverterSettings.getMaxVoltage(i));
                        lastY = Math.round((MiddleY - (GraphicSettings.getOffsetVoltage(i) + locData[i].get(PixelCounter[i])) * dy));
                        currentX = (int) (PixelCounter[i] * dx);
                        System.out.println("locData[" + i + "]:" + locData[i].get(-i + 2));
                        PixelCounter[i]++;
                        ChartLine[i].setColor(GraphicSettings.getChannelColor(i));
                        for (; PixelCounter[i] <= MaximumIncrement && PixelCounter[i] < locData[i].size(); PixelCounter[i]++) {
                            currentY = Math.round(MiddleY - (GraphicSettings.getOffsetVoltage(i) + locData[i].get(PixelCounter[i])) * dy);
                            ChartLine[i].drawLine(currentX, (int) lastY, (int) (currentX + dx), (int) (currentY));
                            lastY = currentY;
                            currentX = (int) (PixelCounter[i] * dx);
                        }
                    }
                }
            }
        }
    }
}
