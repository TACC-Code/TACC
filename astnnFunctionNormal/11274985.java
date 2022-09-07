class BackupThread extends Thread {
    private static int getChannels(Info vorbisInfo) {
        if (vorbisInfo.channels == 1) return AL10.AL_FORMAT_MONO16;
        return AL10.AL_FORMAT_STEREO16;
    }
}
