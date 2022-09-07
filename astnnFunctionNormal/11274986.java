class BackupThread extends Thread {
    private static int getChannels(WavInputStream format) {
        if (format.channels() == 1) {
            if (format.depth() == 8) {
                return AL10.AL_FORMAT_MONO8;
            } else if (format.depth() == 16) {
                return AL10.AL_FORMAT_MONO16;
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else if (format.channels() == 2) {
            if (format.depth() == 8) {
                return AL10.AL_FORMAT_STEREO8;
            } else if (format.depth() == 16) {
                return AL10.AL_FORMAT_STEREO16;
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else {
            throw new JmeException("Only mono or stereo is supported");
        }
    }
}
