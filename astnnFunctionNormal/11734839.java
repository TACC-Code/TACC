class BackupThread extends Thread {
    public synchronized void send(MidiDevice device, MidiMessage message, long timeStamp) {
        if (this.useMIDIPageChanging) {
            if (message instanceof ShortMessage) {
                ShortMessage msg = (ShortMessage) message;
                int velocity = msg.getData1();
                if (msg.getCommand() == ShortMessage.NOTE_ON && velocity > 0) {
                    int channel = msg.getChannel();
                    int note = msg.getData1();
                    for (int j = 0; j < this.pageChangeMidiInDevices.length; j++) {
                        if (this.pageChangeMidiInDevices[j] == null) {
                            continue;
                        }
                        if (this.pageChangeMidiInDevices[j].compareTo(device.getDeviceInfo().getName()) == 0) {
                            for (int i = 0; i < this.midiPageChangeRules.size(); i++) {
                                MIDIPageChangeRule mpcr = this.midiPageChangeRules.get(i);
                                if (mpcr.checkNoteRule(note, channel) == true) {
                                    int switchToPageIndex = mpcr.getPageIndex();
                                    Page page = this.pages.get(switchToPageIndex);
                                    this.switchPage(page, switchToPageIndex, true);
                                }
                            }
                        }
                    }
                }
                if (msg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                    int cc = msg.getData1();
                    int ccVal = msg.getData2();
                    int channel = msg.getChannel();
                    for (int j = 0; j < this.pageChangeMidiInDevices.length; j++) {
                        if (this.pageChangeMidiInDevices[j] == null) {
                            continue;
                        }
                        if (this.pageChangeMidiInDevices[j].compareTo(device.getDeviceInfo().getName()) == 0) {
                            for (int i = 0; i < this.midiPageChangeRules.size(); i++) {
                                MIDIPageChangeRule mpcr = this.midiPageChangeRules.get(i);
                                if (mpcr.checkCCRule(cc, ccVal, channel) == true) {
                                    int switchToPageIndex = mpcr.getPageIndex();
                                    Page page = this.pages.get(switchToPageIndex);
                                    this.switchPage(page, switchToPageIndex, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < this.numPages; i++) {
            for (int j = 0; j < this.midiInDevices[i].length; j++) {
                if (this.midiInDevices[i][j] == null) {
                    continue;
                }
                if (this.midiInDevices[i][j].compareTo(device.getDeviceInfo().getName()) == 0) {
                    this.pages.get(i).send(message, timeStamp);
                }
            }
        }
    }
}
