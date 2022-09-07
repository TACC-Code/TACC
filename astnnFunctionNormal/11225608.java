class BackupThread extends Thread {
    private void addNote(MIDINote note, long noteStart_ppq, long bufferEnd_ppq) throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        for (Track midiTrack : trackMap.values()) {
            on.setMessage(ShortMessage.NOTE_ON, midiTrack.getChannel(), note.pitch, NOTE_VELOCITY);
            midiTrack.addMidiMessage(noteStart_ppq, on);
        }
        ShortMessage off = new ShortMessage();
        if ((noteStart_ppq + note.length) >= bufferEnd_ppq) {
            off.setMessage(ShortMessage.NOTE_OFF, 0, note.pitch, NOTE_VELOCITY);
            midiEventArrayList.add(new MIDIEvent(off, noteStart_ppq + note.length));
        } else {
            for (Track midiTrack : trackMap.values()) {
                off.setMessage(ShortMessage.NOTE_OFF, midiTrack.getChannel(), note.pitch, NOTE_VELOCITY);
                midiTrack.addMidiMessage(noteStart_ppq + note.length, off);
            }
        }
    }
}
