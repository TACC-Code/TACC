class BackupThread extends Thread {
    @Test
    public void interpreteDimmerNotPatched() {
        line("DIMMER 1 Dimmer1 0");
        interprete();
        assertEquals(show.getDimmers().get(0).getChannelId(), -1);
    }
}
