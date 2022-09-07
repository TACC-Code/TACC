class BackupThread extends Thread {
    @Test
    public void interpreteDimmer() {
        line("DIMMER 1 \"dimmer one\" 2");
        interprete();
        assertEquals(show.getDimmers().get(0).getName(), "dimmer one");
        assertEquals(show.getDimmers().get(1).getName(), "Dimmer 2");
        assertEquals(show.getDimmers().get(0).getChannelId(), 1);
        assertEquals(show.getDimmers().get(1).getChannelId(), -1);
    }
}
