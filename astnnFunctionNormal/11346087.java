class BackupThread extends Thread {
    @Test
    public void initialContents_Group() {
        group.setEnabled(true);
        int expectedRowCount = 2 + NUMBER_OF_SUBMASTERS + 1;
        assertEquals(model.getRowCount(), expectedRowCount);
        lightCueDetail1.getTiming().setWaitTime(Time.TIME_1S);
        lightCueDetail1.getSubmasterLevel(0).getLevelValue().setValue(1f);
        lightCueDetail1.getSubmasterLevel(1).getLevelValue().setValue(0.5f);
        lightCueDetail1.getChannelLevel(0).setChannelValue(0.25f);
        lightCueDetail1.getChannelLevel(1).setChannelValue(0.50f);
        lightCueDetail1.getChannelLevel(2).setChannelValue(0.75f);
        lightCueDetail1.getChannelLevel(3).setChannelValue(1.00f);
        assertEquals(((CueChannelLevel) model.getValueAt(3, 0)).getChannelIntValue(), 50);
        assertEquals(((CueChannelLevel) model.getValueAt(4, 0)).getChannelIntValue(), 100);
    }
}
