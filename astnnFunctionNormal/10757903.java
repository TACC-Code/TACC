class BackupThread extends Thread {
    private void open() {
        if (file.endsWith(".ogg")) stream = new OggAudioStream(file);
        if (stream.getChannels() == 1) format = AL.AL_FORMAT_MONO16; else format = AL.AL_FORMAT_STEREO16;
        int[] t_s = new int[1];
        al.alGenBuffers(2, buffers, 0);
        al.alGenSources(1, t_s, 0);
        source = t_s[0];
    }
}
