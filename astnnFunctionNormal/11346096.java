class BackupThread extends Thread {
    @Test
    public void makeSubmasterCellDerived() {
        Submaster submaster1 = context.getShow().getSubmasters().get(0);
        Level level1 = submaster1.getLevel(0);
        level1.setIntValue(30);
        CueChannelLevel channelLevel = lightCueDetail2.getChannelLevel(0);
        CueSubmasterLevel submasterLevel = lightCueDetail2.getSubmasterLevel(0);
        model.setSelected(1, 0, true);
        model.setValueAt("100", 1, 0);
        model.setSelected(1, 0, false);
        model.setSelected(1, 1, true);
        model.setValueAt("50", 1, 1);
        assertFalse(submasterLevel.isDerived());
        assertEquals(submasterLevel.getIntValue(), 50);
        assertEquals(channelLevel.getSubmasterIntValue(), 15);
        model.setValueAt("", 1, 1);
        assertTrue(submasterLevel.isDerived());
        assertEquals(submasterLevel.getIntValue(), 100);
        assertEquals(channelLevel.getSubmasterIntValue(), 30);
    }
}
