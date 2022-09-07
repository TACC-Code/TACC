class BackupThread extends Thread {
    @Override
    public void send(MidiMessage mess, long timeStamp) {
        if (mess.getStatus() >= ShortMessage.MIDI_TIME_CODE) {
            return;
        }
        if (mess instanceof ShortMessage) {
            ShortMessage shm = (ShortMessage) mess;
            if (isLinux) {
                if (shm.getStatus() == ShortMessage.PITCH_BEND) {
                    short low = (byte) shm.getData1();
                    short high = (byte) shm.getData2();
                    int channel = shm.getChannel();
                    low = (byte) shm.getData1();
                    high = (byte) shm.getData2();
                    high = (short) ((high + 64) & 0x007f);
                    try {
                        shm.setMessage(ShortMessage.PITCH_BEND, channel, low, high);
                    } catch (InvalidMidiDataException e) {
                        e.printStackTrace();
                    }
                }
            }
            int cmd = shm.getCommand();
            int dat2 = shm.getData2();
            if (cmd == ShortMessage.NOTE_ON && dat2 == 0) {
                cmd = ShortMessage.NOTE_OFF;
            } else if (cmd == ShortMessage.NOTE_OFF) {
                dat2 = 0;
            }
            int chn = shm.getChannel();
            int dat1 = shm.getData1();
            if (!velSense && cmd == ShortMessage.NOTE_ON) {
                dat2 = 120;
                try {
                    shm.setMessage(cmd, chn, dat1, dat2);
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(ControllerHub.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (debug) {
                System.out.println(String.format(" cmd: %3d chn: %3d  data: %3d  %3d \n", cmd, chn, dat1, dat2));
            }
            if (router.consume(mess, timeStamp)) {
                return;
            }
            if (mono) {
                if (cmd == ShortMessage.NOTE_ON) {
                    if (noteOffMessage != null) {
                        recv.send(noteOffMessage, -1);
                    }
                    noteOffMessage = noteOffMessageX;
                    try {
                        noteOffMessage.setMessage(ShortMessage.NOTE_OFF, chan, dat1, 0);
                    } catch (InvalidMidiDataException ex) {
                        Logger.getLogger(ControllerHub.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            recv.send(mess, -1);
        }
    }
}
