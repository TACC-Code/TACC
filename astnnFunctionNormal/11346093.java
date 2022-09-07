class BackupThread extends Thread {
    @Test
    public void resetChannelLevel() {
        lightCueDetail1.getChannelLevel(0).setDerived(true);
        model.setValueAt("", 3, 1);
        assertTrue(lightCueDetail1.getChannelLevel(0).isDerived());
    }
}
