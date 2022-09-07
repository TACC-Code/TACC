class BackupThread extends Thread {
    @Test
    public void testEx5c2p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c2_p.wav", 33024, "ec3ec5b50c8ee47df0043b0a903e4d49");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
