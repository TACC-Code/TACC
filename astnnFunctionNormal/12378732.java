class BackupThread extends Thread {
    @Test
    public void testEx5c1() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c1.wav", 17024, "e3113334cf3b347607ede08ddcfd0134");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
