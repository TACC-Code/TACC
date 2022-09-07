class BackupThread extends Thread {
    protected void assertTestfileFlv(MediaFile file) {
        assertEquals(TEST_VIDEO_FLV, file.getFilename());
        assertTrue(file.getPath().endsWith(TEST_VIDEO_XUGGLE_PATH));
        assertTrue(file.getSize() > 0);
        assertTrue(file.getDuration() > 0);
        assertEquals(2, file.getStreamCount());
        MediaStream s1 = file.getStreams().get(0);
        assertNotNull(s1);
        assertEquals(0, s1.getStreamIndex());
        assertEquals(0, s1.getStreamType());
        assertEquals(22, s1.getCodecId());
        assertEquals(0, s1.getCodecType());
        assertEquals(424, s1.getWidth());
        assertEquals(176, s1.getHeight());
        assertEquals(0, s1.getBitrate());
        assertEquals(15, s1.getFrameRateNum());
        assertEquals(1, s1.getFrameRateDen());
        assertEquals(0, s1.getSampleRate());
        assertEquals(0, s1.getChannels());
        assertEquals(Long.MIN_VALUE, s1.getDuration().longValue());
        MediaStream s2 = file.getStreams().get(1);
        assertNotNull(s2);
        assertEquals(1, s2.getStreamIndex());
        assertEquals(1, s2.getStreamType());
        assertEquals(86017, s2.getCodecId());
        assertEquals(1, s2.getCodecType());
        assertEquals(0, s2.getWidth());
        assertEquals(0, s2.getHeight());
        assertEquals(64000, s2.getBitrate());
        assertEquals(0, s2.getFrameRateNum());
        assertEquals(0, s2.getFrameRateDen());
        assertEquals(22050, s2.getSampleRate());
        assertEquals(1, s2.getChannels());
        assertEquals(Long.MIN_VALUE, s2.getDuration().longValue());
    }
}
