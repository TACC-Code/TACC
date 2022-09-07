class BackupThread extends Thread {
    private void fxSendSpinnerStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.FX_ASSIGN, ((Integer) fxSendSpinner.getValue()).intValue());
    }
}
