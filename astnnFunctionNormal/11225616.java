class BackupThread extends Thread {
    private void outputStoredNotes(long bufferStart_ppq, long bufferEnd_ppq) throws InvalidMidiDataException {
        Iterator<MIDIEvent> iter = midiEventArrayList.iterator();
        while (iter.hasNext()) {
            MIDIEvent tempMidiEvent = iter.next();
            if (tempMidiEvent.getTimeStamp() < bufferStart_ppq) {
                iter.remove();
            } else if (tempMidiEvent.getTimeStamp() < bufferEnd_ppq) {
                for (Track midiTrack : trackMap.values()) {
                    ShortMessage shortMsg = tempMidiEvent.getMessage();
                    shortMsg.setMessage(shortMsg.getCommand(), midiTrack.getChannel(), shortMsg.getData1(), shortMsg.getData2());
                    midiTrack.addMidiMessage(tempMidiEvent.getTimeStamp(), shortMsg);
                }
                iter.remove();
            }
        }
    }
}
