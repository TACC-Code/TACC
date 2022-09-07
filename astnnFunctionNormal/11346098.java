class BackupThread extends Thread {
    @Test
    public void makeChannelCellNotDerived_Group() {
        group.setEnabled(true);
        model.setSelected(3, 1, true);
        int[] rows = { 3 };
        int[] cols = { 1 };
        model.keyAction(CellAction.ACTION_SHIFT_UP, rows, cols);
        CueChannelLevel level = lightCueDetail2.getChannelLevel(1);
        assertFalse(level.isDerived());
        assertEquals(level.getChannelIntValue(), 10);
    }
}
