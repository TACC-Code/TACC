class BackupThread extends Thread {
    @Test
    public void interpreteCue() {
        line("SUBMASTER 1 Submaster1 10 20");
        line("SUBMASTER 2 Submaster2 10 20");
        line("SUBMASTER 3 Submaster3 10 20");
        line("SUBMASTER 4 Submaster4 10 20");
        line("CUE \"number\" \"page\" \"prompt\" \"L 5\" 50 x - 40 x 50");
        interprete();
        Cue cue = show.getCues().get(0);
        assertEquals(cue.getNumber(), "number");
        assertEquals(cue.getPage(), "page");
        assertEquals(cue.getPrompt(), "prompt");
        LightCueDetail detail = (LightCueDetail) cue.getDetail();
        assertEquals(detail.getTiming().getFadeInTime(), Time.TIME_5S);
        assertFalse(detail.getSubmasterLevel(0).isDerived());
        assertTrue(detail.getSubmasterLevel(1).isDerived());
        assertFalse(detail.getSubmasterLevel(2).isDerived());
        assertFalse(detail.getSubmasterLevel(3).isDerived());
        assertTrue(detail.getChannelLevel(0).isDerived());
        assertFalse(detail.getChannelLevel(1).isDerived());
        assertEquals(detail.getSubmasterLevel(0).getIntValue(), 50);
        assertEquals(detail.getSubmasterLevel(1).getIntValue(), 0);
        assertEquals(detail.getSubmasterLevel(2).getIntValue(), 0);
        assertEquals(detail.getSubmasterLevel(3).getIntValue(), 40);
        assertEquals(detail.getChannelLevel(0).getChannelIntValue(), 0);
        assertEquals(detail.getChannelLevel(1).getChannelIntValue(), 50);
        assertEquals(detail.getChannelLevel(0).getSubmasterIntValue(), 5);
        assertEquals(detail.getChannelLevel(1).getSubmasterIntValue(), 10);
        assertTrue(detail.getChannelLevel(0).getChannelLevelValue().isActive());
        assertTrue(detail.getChannelLevel(1).getChannelLevelValue().isActive());
    }
}
