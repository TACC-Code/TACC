class BackupThread extends Thread {
    public MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return getMidiFileFormat(is);
    }
}
