class BackupThread extends Thread {
    private void fxSendKnobStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.FX_SEND_LEVEL, fxSendKnob.getIntValue());
    }
}
