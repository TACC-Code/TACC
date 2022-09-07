class BackupThread extends Thread {
    private void setUseCoeff(final boolean state) throws DataException {
        if (state) {
            setUILabels(state);
            getChannels();
            calculateCoefficients();
            text1.setText(format(intercept1));
            text2.setText(format(slope1));
            text3.setText(format(intercept2));
            text4.setText(format(slope2));
        } else {
            setUILabels(state);
            getCoefficients();
            calculateChannels();
            text1.setText(format(chan1i));
            text2.setText(format(chan1f));
            text3.setText(format(chan2i));
            text4.setText(format(chan2f));
        }
    }
}
