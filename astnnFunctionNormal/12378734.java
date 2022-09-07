class BackupThread extends Thread {
    @Test
    public void testEx5c2() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c2.wav", 17024, "f7df3b17aad8337f9748fdd7fb8359de");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
