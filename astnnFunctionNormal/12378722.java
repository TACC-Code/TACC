class BackupThread extends Thread {
    @Test
    public void testEx101() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex1_01.wav");
        assertEquals(16000, ais.getFrameLength());
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
        assertEquals(16000.0f, format.getFrameRate(), 0.0);
        assertEquals(2, format.getFrameSize());
        assertFalse(format.isBigEndian());
        assertEquals(16000.0f, format.getSampleRate(), 0.0);
        assertEquals(16, format.getSampleSizeInBits());
        assertFalse(format.isBigEndian());
    }
}
