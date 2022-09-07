class BackupThread extends Thread {
    @Override
    public MidiSoundContainer loadSound(URL url) throws IOException {
        return (loadSound(url.openStream()));
    }
}
