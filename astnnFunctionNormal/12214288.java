class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == chan || e.getSource() == ctrl) {
                    if (midiLocation instanceof MidiInputLocation) {
                        mproc.setMidiInputLocation(midiLocation.getChannel(), midiLocation.getCtrlType(), ((Integer) chan.getSelectedItem()).intValue(), ((Integer) ctrl.getSelectedItem()).intValue());
                    } else if (midiLocation instanceof MidiOutputLocation) {
                        midiLocation.setMidiController(((Integer) chan.getSelectedItem()).intValue(), ((Integer) ctrl.getSelectedItem()).intValue());
                    }
                }
            }
}
