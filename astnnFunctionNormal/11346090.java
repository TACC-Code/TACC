class BackupThread extends Thread {
    @Test
    public void updateChannelLevel() {
        model.setSelected(3, 0, true);
        model.setValueAt("10", 3, 0);
        assertEquals(lightCueDetail1.getChannelLevel(0).getChannelIntValue(), 10);
    }
}
