class BackupThread extends Thread {
    void setToNoteOff() {
        try {
            command = ShortMessage.NOTE_OFF;
            setMessage(command, getChannel(), getData1(), getData2());
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
