class BackupThread extends Thread {
    public ReadSound(String filename) throws Exception {
        File soundFile = new File(filename);
        audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        audioFormat = audioInputStream.getFormat();
        channels = audioFormat.getChannels();
        bufferChannelFile = new Vector[this.channels];
        for (int i = 0; i < this.channels; i++) bufferChannelFile[i] = new Vector<Byte>();
        abData = new byte[channels * PACKET_SIZE];
    }
}
