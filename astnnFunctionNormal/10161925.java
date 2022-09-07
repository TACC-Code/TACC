class BackupThread extends Thread {
    protected void assertTestfileFlvFromProfile1(MediaFile file) {
        assertEquals("testfile.mp4", file.getFilename());
        assertEquals("/tmp/", file.getPath());
        assertTrue(file.getSize() == 0);
        assertTrue(file.getDuration() > 0);
        assertEquals(2, file.getStreamCount());
        MediaStream s1 = file.getStreams().get(0);
        assertNotNull(s1);
        assertEquals(0, s1.getStreamIndex());
        assertEquals(0, s1.getStreamType());
        assertEquals(28, s1.getCodecId());
        assertEquals(0, s1.getCodecType());
        assertEquals(320, s1.getWidth());
        assertEquals(240, s1.getHeight());
        assertEquals(1024000, s1.getBitrate());
        assertEquals(30, s1.getFrameRateNum());
        assertEquals(1, s1.getFrameRateDen());
        assertEquals(0, s1.getSampleRate());
        assertEquals(0, s1.getChannels());
        assertEquals(0, s1.getDuration().longValue());
        MediaStream s2 = file.getStreams().get(1);
        assertNotNull(s2);
        assertEquals(1, s2.getStreamIndex());
        assertEquals(1, s2.getStreamType());
        assertEquals(86016, s2.getCodecId());
        assertEquals(1, s2.getCodecType());
        assertEquals(0, s2.getWidth());
        assertEquals(0, s2.getHeight());
        assertEquals(128000, s2.getBitrate());
        assertEquals(0, s2.getFrameRateNum());
        assertEquals(0, s2.getFrameRateDen());
        assertEquals(44100, s2.getSampleRate());
        assertEquals(2, s2.getChannels());
        assertEquals(0, s2.getDuration().longValue());
    }
}
