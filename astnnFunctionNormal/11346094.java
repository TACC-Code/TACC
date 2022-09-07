class BackupThread extends Thread {
    @Test
    public void resetChannelLevel_Group() {
        lightCueDetail1.getChannelLevel(0).setDerived(false);
        lightCueDetail1.getChannelLevel(1).setDerived(false);
        lightCueDetail1.getChannelLevel(2).setDerived(false);
        lightCueDetail1.getChannelLevel(3).setDerived(false);
        group.setEnabled(true);
        model.setSelected(3, 0, true);
        model.setValueAt("", 3, 0);
        assertFalse(lightCueDetail1.getChannelLevel(3).isDerived());
        assertTrue(lightCueDetail1.getChannelLevel(1).isDerived());
        assertFalse(lightCueDetail1.getChannelLevel(3).isDerived());
        assertFalse(lightCueDetail1.getChannelLevel(3).isDerived());
    }
}
