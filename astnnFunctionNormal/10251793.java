class BackupThread extends Thread {
    public static AudioFormat getCopyOfAudioFormat(AudioFormat old_audio_format) {
        return new AudioFormat(old_audio_format.getEncoding(), old_audio_format.getSampleRate(), old_audio_format.getSampleSizeInBits(), old_audio_format.getChannels(), old_audio_format.getFrameSize(), old_audio_format.getFrameRate(), old_audio_format.isBigEndian());
    }
}
