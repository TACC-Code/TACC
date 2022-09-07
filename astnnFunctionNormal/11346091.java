class BackupThread extends Thread {
    @Test
    public void updateChannelLevel_Group() {
        group.setEnabled(true);
        model.setSelected(3, 0, true);
        model.setValueAt("10", 3, 0);
        model.setSelected(3, 0, false);
        model.setSelected(4, 0, true);
        model.setValueAt("20", 4, 0);
        assertEquals(lightCueDetail1.getChannelLevel(1).getChannelIntValue(), 10);
        assertEquals(lightCueDetail1.getChannelLevel(3).getChannelIntValue(), 20);
    }
}
