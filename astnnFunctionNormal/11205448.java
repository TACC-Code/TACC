class BackupThread extends Thread {
    private void ringModKnobStateChanged(ChangeEvent evt) {
        if (getInitProvider() == null || getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitParamChange(this, getChannelNumber(), RackAttack.RING_MOD_LEVEL, ringModKnob.getIntValue());
    }
}
