class BackupThread extends Thread {
    public void testJoinPart() throws Exception {
        aufraeumen();
        ircServer.handleEvent(connetcts[1].getEvent());
        ircServer.handleEvent(connetcts[2].getEvent());
        String channel = channels[0];
        ircServer.handleEvent((new JoinEventParameter(users[1], channel)).getEvent());
        IRCChannelBean c = zustandsabfrage.getChannelWithJoins(channel).getValue();
        assertEquals(1, c.getJoins().size());
        ircServer.handleEvent(new JoinEventParameter(users[2], channel).getEvent());
        c = zustandsabfrage.getChannelWithJoins(channel).getValue();
        assertEquals(2, c.getJoins().size());
        ircServer.handleEvent((new PartEventParameter(users[1], channel)).getEvent());
        ircServer.handleEvent((new PartEventParameter(users[2], channel)).getEvent());
        c = zustandsabfrage.getChannelWithJoins(channel).getValue();
        assertNull(c);
    }
}
