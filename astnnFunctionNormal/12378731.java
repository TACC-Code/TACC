class BackupThread extends Thread {
    @Test
    public void testEx512p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_12_p.wav", 33024, "9a49dabcc340912a76057b687c29d51a");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
