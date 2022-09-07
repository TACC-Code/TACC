class BackupThread extends Thread {
    @Test
    public void initialContents() {
        int expectedRowCount = NUMBER_OF_CHANNELS + NUMBER_OF_SUBMASTERS + 1;
        assertEquals(model.getRowCount(), expectedRowCount);
        assertEquals(model.getColumnCount(), 2);
        assertTrue(model.getValueAt(0, 0) instanceof Timing);
        assertTrue(model.getValueAt(1, 0) instanceof CueSubmasterLevel);
        assertTrue(model.getValueAt(2, 0) instanceof CueSubmasterLevel);
        assertTrue(model.getValueAt(3, 0) instanceof CueChannelLevel);
        assertTrue(model.getValueAt(4, 0) instanceof CueChannelLevel);
        assertTrue(model.getValueAt(5, 0) instanceof CueChannelLevel);
        assertTrue(model.getValueAt(6, 0) instanceof CueChannelLevel);
        lightCueDetail1.getTiming().setWaitTime(Time.TIME_1S);
        lightCueDetail1.getSubmasterLevel(0).getLevelValue().setValue(1f);
        lightCueDetail1.getSubmasterLevel(1).getLevelValue().setValue(0.5f);
        lightCueDetail1.getChannelLevel(0).setChannelValue(0.25f);
        lightCueDetail1.getChannelLevel(1).setChannelValue(0.50f);
        lightCueDetail1.getChannelLevel(2).setChannelValue(0.75f);
        lightCueDetail1.getChannelLevel(3).setChannelValue(1.00f);
        assertEquals(((Timing) model.getValueAt(0, 0)).getWaitTime(), Time.TIME_1S);
        assertEquals(((CueSubmasterLevel) model.getValueAt(1, 0)).getIntValue(), 100);
        assertEquals(((CueSubmasterLevel) model.getValueAt(2, 0)).getIntValue(), 50);
        assertEquals(((CueChannelLevel) model.getValueAt(3, 0)).getChannelIntValue(), 25);
        assertEquals(((CueChannelLevel) model.getValueAt(4, 0)).getChannelIntValue(), 50);
        assertEquals(((CueChannelLevel) model.getValueAt(5, 0)).getChannelIntValue(), 75);
        assertEquals(((CueChannelLevel) model.getValueAt(6, 0)).getChannelIntValue(), 100);
    }
}
