class BackupThread extends Thread {
    @Test
    public void testEx5p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_p.wav", 65024, "4db87101722bc8086d103c2479ad6a5d");
        AudioFormat format = ais.getFormat();
        assertEquals(2, format.getChannels());
    }
}
