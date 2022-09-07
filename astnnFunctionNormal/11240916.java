class BackupThread extends Thread {
    private double getNormalisedValue(List<EEGChannelValue> values) {
        double d = 0;
        for (EEGChannelValue value : values) {
            d += value.getChannelStrengthWithCalibration() * value.getChannelStrengthWithCalibration();
        }
        return Math.sqrt(d);
    }
}
