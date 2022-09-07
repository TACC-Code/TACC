class BackupThread extends Thread {
    private void setSignal(EEGChannelValue value) {
        for (FingBrainer fb : fingBrainers) {
            try {
                if (value.isForFrequencyType(fb.getState().getFrequencyType())) {
                    animateFingBrainer(fb, value.getChannelStrengthWithCalibration());
                    return;
                }
            } catch (NullPointerException e) {
            }
        }
    }
}
