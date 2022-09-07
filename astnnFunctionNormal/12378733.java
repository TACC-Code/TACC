class BackupThread extends Thread {
    @Test
    public void testEx5c1p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c1_p.wav", 33024, "a95d9a49344c822f8a60e5bcd48ca982");
        AudioFormat format = ais.getFormat();
        assertEquals(1, format.getChannels());
    }
}
