class BackupThread extends Thread {
    void setToNoteOn() {
        try {
            command = ShortMessage.NOTE_ON;
            setMessage(command, getChannel(), getData1(), getData2());
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
