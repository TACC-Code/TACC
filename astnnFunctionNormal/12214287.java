class BackupThread extends Thread {
    public MidiLocationSetter() {
        super();
        ActionListener cal = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == chan || e.getSource() == ctrl) {
                    if (midiLocation instanceof MidiInputLocation) {
                        mproc.setMidiInputLocation(midiLocation.getChannel(), midiLocation.getCtrlType(), ((Integer) chan.getSelectedItem()).intValue(), ((Integer) ctrl.getSelectedItem()).intValue());
                    } else if (midiLocation instanceof MidiOutputLocation) {
                        midiLocation.setMidiController(((Integer) chan.getSelectedItem()).intValue(), ((Integer) ctrl.getSelectedItem()).intValue());
                    }
                }
            }
        };
        for (int i = 1; i < 17; i++) {
            chan.addItem(new Integer(i));
        }
        for (int i = 0; i < 128; i++) {
            ctrl.addItem(new Integer(i));
        }
        chan.addActionListener(cal);
        ctrl.addActionListener(cal);
    }
}
