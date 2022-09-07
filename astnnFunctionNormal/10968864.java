class BackupThread extends Thread {
    @Override
    public OggSoundContainer loadSound(URL url) throws IOException {
        return (loadSound(url.openStream()));
    }
}
