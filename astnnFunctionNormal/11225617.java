class BackupThread extends Thread {
    private void outputStoredNotes() {
        try {
            Iterator<MIDIEvent> iter = midiEventArrayList.iterator();
            while (iter.hasNext()) {
                MIDIEvent tempMidiEvent = iter.next();
                for (Track midiTrack : trackMap.values()) {
                    ShortMessage shortMsg = tempMidiEvent.getMessage();
                    shortMsg.setMessage(shortMsg.getCommand(), midiTrack.getChannel(), shortMsg.getData1(), shortMsg.getData2());
                    midiTrack.addMidiMessage(tempMidiEvent.getTimeStamp(), shortMsg);
                }
                iter.remove();
            }
        } catch (InvalidMidiDataException ex) {
            MsgHandler.critical("PianoRollAgentException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
