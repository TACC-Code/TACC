class BackupThread extends Thread {
        @Override
        protected void filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {
            sendNow(message);
            if (_duration < 1) {
                return;
            }
            if (!(message instanceof ShortMessage)) {
                return;
            }
            ShortMessage msg = (ShortMessage) message;
            int command = msg.getCommand();
            if (command < 128 || command > 224) {
                return;
            }
            int channel = msg.getChannel();
            int note = msg.getData1();
            int velocity = msg.getData2();
            float milliDelay = (60000 / (_tempo * _interval.getNotesPerBeat()));
            int[] velocities;
            if (ShortMessage.NOTE_ON == command) {
                velocities = calculateDecay(velocity);
            } else {
                velocities = new int[_duration];
            }
            for (int i = 0; i < _duration; i++) {
                ShortMessage schedNote = new ShortMessage();
                schedNote.setMessage(command, channel, note, velocities[i]);
                long delay = (long) milliDelay * (i + 1);
                sendLater(schedNote, delay);
            }
        }
}
