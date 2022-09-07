class BackupThread extends Thread {
    public void testWavFormat() {
        {
            final WavAudioFormat f = new WavAudioFormat("abc");
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getSampleRate(), Format.NOT_SPECIFIED);
            assertEquals(f.getSampleSizeInBits(), Format.NOT_SPECIFIED);
            assertEquals(f.getChannels(), Format.NOT_SPECIFIED);
            assertEquals(f.getFrameSizeInBits(), Format.NOT_SPECIFIED);
            assertEquals(f.getAverageBytesPerSecond(), Format.NOT_SPECIFIED);
            assertEquals(f.getFrameRate(), Format.NOT_SPECIFIED);
            assertEquals(f.getEndian(), Format.NOT_SPECIFIED);
            assertEquals(f.getSigned(), Format.NOT_SPECIFIED);
            assertEquals(f.getDataType(), Format.byteArray);
        }
        {
            final WavAudioFormat f = new WavAudioFormat("abc", 1.0, 2, 3, 4, 5, 6, 7, 8.f, Format.byteArray, new byte[] { (byte) 0 });
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getSampleRate(), 1.0);
            assertEquals(f.getSampleSizeInBits(), 2);
            assertEquals(f.getChannels(), 3);
            assertEquals(f.getFrameSizeInBits(), 4);
            assertEquals(f.getAverageBytesPerSecond(), 5);
            assertEquals(f.getFrameRate(), 5.0);
            assertEquals(f.getEndian(), 6);
            assertEquals(f.getSigned(), 7);
            assertEquals(f.getDataType(), Format.byteArray);
        }
        {
            final WavAudioFormat f = new WavAudioFormat("abc", 1.0, 2, 3, 4, 10, 6, 7, 8.f, Format.byteArray, new byte[] { (byte) 0 });
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getSampleRate(), 1.0);
            assertEquals(f.getSampleSizeInBits(), 2);
            assertEquals(f.getChannels(), 3);
            assertEquals(f.getFrameSizeInBits(), 4);
            assertEquals(f.getAverageBytesPerSecond(), 10);
            assertEquals(f.getFrameRate(), 10.0);
            assertEquals(f.getEndian(), 6);
            assertEquals(f.getSigned(), 7);
            assertEquals(f.getDataType(), Format.byteArray);
        }
        {
            final WavAudioFormat f = new WavAudioFormat("abc", 1.0, 2, 3, 4, Format.NOT_SPECIFIED, 6, 7, 8.f, Format.byteArray, new byte[] { (byte) 0 });
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getSampleRate(), 1.0);
            assertEquals(f.getSampleSizeInBits(), 2);
            assertEquals(f.getChannels(), 3);
            assertEquals(f.getFrameSizeInBits(), 4);
            assertEquals(f.getAverageBytesPerSecond(), Format.NOT_SPECIFIED);
            assertEquals(f.getFrameRate(), Format.NOT_SPECIFIED);
            assertEquals(f.getEndian(), 6);
            assertEquals(f.getSigned(), 7);
            assertEquals(f.getDataType(), Format.byteArray);
        }
        {
            final WavAudioFormat f = new WavAudioFormat("abc", 1.0, 2, 3, 4, 5, new byte[] { (byte) 0 });
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getSampleRate(), 1.0);
            assertEquals(f.getSampleSizeInBits(), 2);
            assertEquals(f.getChannels(), 3);
            assertEquals(f.getFrameSizeInBits(), 4);
            assertEquals(f.getAverageBytesPerSecond(), 5);
            assertEquals(f.getFrameRate(), 5.0);
            assertEquals(f.getEndian(), Format.NOT_SPECIFIED);
            assertEquals(f.getSigned(), Format.NOT_SPECIFIED);
            assertEquals(f.getDataType(), Format.byteArray);
        }
    }
}
