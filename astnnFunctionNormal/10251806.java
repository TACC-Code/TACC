class BackupThread extends Thread {
    public static AudioFormat getConvertedAudioFormat(AudioFormat original_format) {
        int bit_depth = original_format.getSampleSizeInBits();
        if (bit_depth != 8 && bit_depth != 16) bit_depth = 16;
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, original_format.getSampleRate(), bit_depth, original_format.getChannels(), original_format.getChannels() * (bit_depth / 8), original_format.getSampleRate(), true);
    }
}
