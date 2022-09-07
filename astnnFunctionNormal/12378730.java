class BackupThread extends Thread {
    @Test
    public void testEx512() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_12.wav", 17024, "8b19477ffdc4ed94abb65af950bb4cf8");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
