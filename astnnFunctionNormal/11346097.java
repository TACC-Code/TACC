class BackupThread extends Thread {
    @Test
    public void makeChannelCellNotDerived() {
        CueChannelLevel level = lightCueDetail2.getChannelLevel(0);
        assertTrue(level.isDerived());
        assertEquals(level.getChannelIntValue(), 0);
        model.setSelected(3, 1, true);
        int[] rows = { 3 };
        int[] cols = { 1 };
        model.keyAction(CellAction.ACTION_SHIFT_UP, rows, cols);
        assertFalse(level.isDerived());
        assertEquals(level.getChannelIntValue(), 10);
    }
}
