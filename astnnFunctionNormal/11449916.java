class BackupThread extends Thread {
    public TtsTrackImpl(String text, String voiceName, VoicesCache vc) {
        this.voiceCache = vc;
        isReady = false;
        voice = voiceCache.allocateVoice(voiceName);
        TTSAudioBuffer audioBuffer = new TTSAudioBuffer();
        voice.setAudioPlayer(audioBuffer);
        voice.speak(text);
        audioBuffer.flip();
        frameSize = (int) (period * format.getChannels() * format.getSampleSizeInBits() * format.getSampleRate() / 8000);
    }
}
