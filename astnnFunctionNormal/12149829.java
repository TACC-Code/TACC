class BackupThread extends Thread {
    public void testDoppelJoin() throws Exception {
        aufraeumen();
        String channel = channels[0];
        ircServer.handleEvent(connetcts[1].getEvent());
        ircServer.handleEvent((new JoinEventParameter(users[1], channel)).getEvent());
        IRCChannelBean c = zustandsabfrage.getChannelWithJoins(channel).getValue();
        assertEquals(1, c.getJoins().size());
        ReturnValue retval = ircServer.handleEvent((new JoinEventParameter(users[1], channel)).getEvent());
        assertNull(retval.getValue());
        assertTrue(retval.getMessages().contains(new Message(Labels.ALREADY_EXISTS, Sources.JOIN)));
    }
}
