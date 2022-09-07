class BackupThread extends Thread {
    private void panKnobStateChanged(ChangeEvent evt) {
        if (getInitProvider().isInitiating()) {
            return;
        }
        MidiThread.getInstance().emitPanChange(this, getChannelNumber(), panKnob.getIntValue());
    }
}
