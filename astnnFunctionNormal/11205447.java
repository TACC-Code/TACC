class BackupThread extends Thread {
    private void crackKnobStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.CRACK_LEVEL, crackKnob.getIntValue());
    }
}
