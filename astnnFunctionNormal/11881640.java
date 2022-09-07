class BackupThread extends Thread {
    private void doGainShift() throws DataException {
        if (cchan.isSelected()) {
            getChannels();
            calculateCoefficients();
        } else {
            getCoefficients();
        }
        final HistogramType oneDi = HistogramType.ONE_DIM_INT;
        final boolean isOneD = hfrom.getType() == oneDi;
        final double[] countsIn = isOneD ? this.numberUtilities.intToDoubleArray(((jam.data.HistInt1D) hfrom).getCounts()) : ((jam.data.HistDouble1D) hfrom).getCounts();
        final double[] errIn = hfrom.getErrors();
        getOrCreateOutputHistogram();
        hto.setZero();
        final int countLen = hto.getType() == oneDi ? ((jam.data.HistInt1D) hto).getCounts().length : ((jam.data.HistDouble1D) hto).getCounts().length;
        final double[] out = gainShift(countsIn, intercept1, slope1, intercept2, slope2, countLen);
        final double[] errOut = errorGainShift(errIn, intercept1, slope1, intercept2, slope2, hto.getErrors().length);
        if (hto.getType() == oneDi) {
            hto.setCounts(this.numberUtilities.doubleToIntArray(out));
        } else {
            hto.setCounts(out);
        }
        hto.setErrors(errOut);
        LOGGER.info("Gain shift " + hfrom.getFullName().trim() + " with gain: " + format(intercept1) + " + " + format(slope1) + " x ch; to " + hto.getFullName() + " with gain: " + format(intercept2) + " + " + format(slope2) + " x ch");
    }
}
