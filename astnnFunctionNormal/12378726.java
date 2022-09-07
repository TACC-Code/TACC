class BackupThread extends Thread {
    @Test
    public void testEx4() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex4.wav");
        AudioFormat format = ais.getFormat();
        assertEquals(Encoding.ULAW, format.getEncoding());
        assertEquals(1, format.getChannels());
        assertEquals(16000.0f, format.getSampleRate(), 0.0);
    }
}
