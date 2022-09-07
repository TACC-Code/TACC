class BackupThread extends Thread {
    @Test
    public void makeChannelCellDerived() {
        model.setSelected(3, 0, true);
        model.setValueAt("10", 3, 0);
        model.setSelected(3, 0, false);
        model.setSelected(3, 1, true);
        model.setValueAt("20", 3, 1);
        CueChannelLevel level = lightCueDetail2.getChannelLevel(0);
        assertFalse(level.isDerived());
        assertEquals(level.getChannelIntValue(), 20);
        model.setValueAt("", 3, 1);
        assertTrue(level.isDerived());
        assertEquals(level.getChannelIntValue(), 10);
    }
}
