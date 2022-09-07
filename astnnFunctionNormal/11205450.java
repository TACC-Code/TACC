class BackupThread extends Thread {
    private void osc1KnobStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.OSC1_LEVEL, osc1Knob.getIntValue());
    }
}
