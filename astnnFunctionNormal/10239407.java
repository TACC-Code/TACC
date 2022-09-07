class BackupThread extends Thread {
    public PCMDecoder(FFXISound sound, ReadableByteChannel in) {
        channels = sound.getChannels();
        frameSize = channels * (sound.getBitsPerSample() >> 3);
        loopOffset = sound.getLoopPoint() * frameSize;
    }
}
