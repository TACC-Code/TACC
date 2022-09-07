class BackupThread extends Thread {
    public static AudioInputStream convertUnsupportedFormat(AudioInputStream audio_input_stream) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audio_input_stream.getFormat());
        if (!AudioSystem.isLineSupported(info)) {
            AudioFormat original_format = audio_input_stream.getFormat();
            int bit_depth = 16;
            AudioFormat new_audio_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, original_format.getSampleRate(), bit_depth, original_format.getChannels(), original_format.getChannels() * (bit_depth / 8), original_format.getSampleRate(), true);
            audio_input_stream = AudioSystem.getAudioInputStream(new_audio_format, audio_input_stream);
        }
        return audio_input_stream;
    }
}
