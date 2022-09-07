class BackupThread extends Thread {
    public static int getNumberBytesNeeded(int number_samples, AudioFormat audio_format) {
        int number_bytes_per_sample = audio_format.getSampleSizeInBits() / 8;
        int number_channels = audio_format.getChannels();
        return (number_samples * number_bytes_per_sample * number_channels);
    }
}
