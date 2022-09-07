class BackupThread extends Thread {
    public static double[][] extractSampleValues(AudioInputStream audio_input_stream) throws Exception {
        byte[] audio_bytes = getBytesFromAudioInputStream(audio_input_stream);
        int number_bytes = audio_bytes.length;
        AudioFormat this_audio_format = audio_input_stream.getFormat();
        int number_of_channels = this_audio_format.getChannels();
        int bit_depth = this_audio_format.getSampleSizeInBits();
        if ((bit_depth != 16 && bit_depth != 8) || !this_audio_format.isBigEndian() || this_audio_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) throw new Exception("Only 8 or 16 bit signed PCM samples with a big-endian\n" + "byte order can be analyzed currently.");
        int number_of_bytes = audio_bytes.length;
        int bytes_per_sample = bit_depth / 8;
        int number_samples = number_of_bytes / bytes_per_sample / number_of_channels;
        if (((number_samples == 2 || bytes_per_sample == 2) && (number_of_bytes % 2 != 0)) || ((number_samples == 2 && bytes_per_sample == 2) && (number_of_bytes % 4 != 0))) throw new Exception("Uneven number of bytes for given bit depth and number of channels.");
        double max_sample_value = AudioMethods.findMaximumSampleValue(bit_depth) + 2.0;
        double[][] sample_values = new double[number_of_channels][number_samples];
        ByteBuffer byte_buffer = ByteBuffer.wrap(audio_bytes);
        if (bit_depth == 8) {
            for (int samp = 0; samp < number_samples; samp++) for (int chan = 0; chan < number_of_channels; chan++) sample_values[chan][samp] = (double) byte_buffer.get() / max_sample_value;
        } else if (bit_depth == 16) {
            ShortBuffer short_buffer = byte_buffer.asShortBuffer();
            for (int samp = 0; samp < number_samples; samp++) for (int chan = 0; chan < number_of_channels; chan++) sample_values[chan][samp] = (double) short_buffer.get() / max_sample_value;
        }
        return sample_values;
    }
}
