class BackupThread extends Thread {
    @Test
    public void interpreteChannel() {
        line("CHANNEL 1 \"channel one\"");
        interprete();
        assertEquals(show.getChannels().get(0).getName(), "channel one");
        assertEquals(show.getChannels().get(1).getName(), "Channel 2");
    }
}
