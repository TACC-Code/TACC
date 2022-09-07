class BackupThread extends Thread {
    private static float getPlayTime(byte[] data, AudioFormat format, int rate) {
        if (format.getChannels() == 1) {
            if (format.getSampleSizeInBits() == 8) {
                return (float) (data.length) / (float) (rate);
            } else if (format.getSampleSizeInBits() == 16) {
                return (float) (data.length) / (float) (rate * 2);
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else if (format.getChannels() == 2) {
            if (format.getSampleSizeInBits() == 8) {
                return (float) (data.length) / (float) (rate * 2);
            } else if (format.getSampleSizeInBits() == 16) {
                return (float) (data.length) / (float) (rate * 4);
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else {
            throw new JmeException("Only mono or stereo is supported");
        }
    }
}
