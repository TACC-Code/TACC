class BackupThread extends Thread {
    @Test
    public void testEx5p01() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_p01.wav", 65024, "6a59a8a072d735feda583787e8203a6a");
        AudioFormat format = ais.getFormat();
        assertEquals(2, format.getChannels());
        assertFalse(format.isBigEndian());
    }
}
