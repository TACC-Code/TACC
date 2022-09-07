class BackupThread extends Thread {
    @Test
    public void testEx6() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex6.wav", 65024, "20f533bb92c820e65f18a16ec0b85075");
        AudioFormat format = ais.getFormat();
        assertEquals(4, format.getChannels());
    }
}
