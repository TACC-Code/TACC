class BackupThread extends Thread {
    private void osc2KnobStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.OSC2_LEVEL, osc2Knob.getIntValue());
    }
}
