class BackupThread extends Thread {
    @Test
    public void testEx5() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5.wav");
        AudioFormat format = ais.getFormat();
        assertEquals(2, format.getChannels());
    }
}
